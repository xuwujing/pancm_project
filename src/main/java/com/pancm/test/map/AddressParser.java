package com.pancm.test.map;

import java.io.*;
import java.net.*;
import java.sql.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pancm.util.MyTools;
/**
 * @Author pancm
 * @Description 获取中国的省市区信息并写入到表中
 * @Date  2023/6/17
 * @Param
 * @return
 **/
public class AddressParser {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pcm_demo";
    private static final String USER = "root";
    private static final String PASS = "123456";

    public static void main(String[] args) {
        try {
            // 发送 GET 请求获取高德地图的中国省市区地址信息 subdistrict=3就是到区级
            String url = "https://restapi.amap.com/v3/config/district?keywords=中国&subdistrict=3&key=44cfa7574786e7f35a7f58b545f57819";
            String jsonStr = sendGet(url);

            // 解析返回的 JSON 数据
            JSONObject jsonObj = MyTools.toJson(jsonStr);
            JSONArray districtArr = jsonObj.getJSONArray("districts");
            JSONObject countryObj = districtArr.getJSONObject(0);
            JSONArray provinceArr = countryObj.getJSONArray("districts");

            // 遍历省份列表，插入省份数据到数据库中
            for (int i = 0; i < provinceArr.size(); i++) {
                JSONObject provinceObj = provinceArr.getJSONObject(i);
                String provinceCode = provinceObj.getString("adcode");
                String provinceName = provinceObj.getString("name");
                insertData("province", provinceCode, provinceName, "CN");

                // 遍历城市列表，插入城市数据到数据库中
                JSONArray cityArr = provinceObj.getJSONArray("districts");
                for (int j = 0; j < cityArr.size(); j++) {
                    JSONObject cityObj = cityArr.getJSONObject(j);
                    String cityCode = cityObj.getString("adcode");
                    String cityName = cityObj.getString("name");
                    insertData("city", cityCode, cityName, provinceCode);

                    // 遍历区县列表，插入区县数据到数据库中
                    JSONArray districtArr2 = cityObj.getJSONArray("districts");
                    for (int k = 0; k < districtArr2.size(); k++) {
                        JSONObject districtObj = districtArr2.getJSONObject(k);
                        String districtCode = districtObj.getString("adcode");
                        String districtName = districtObj.getString("name");
                        insertData("district", districtCode, districtName, cityCode);

                        // 遍历街道列表，插入街道数据到数据库中
                        JSONArray streetArr = districtObj.getJSONArray("districts");
                        for (int l = 0; l < streetArr.size(); l++) {
                            JSONObject streetObj = streetArr.getJSONObject(l);
                            String streetCode = streetObj.getString("adcode");
                            String streetName = streetObj.getString("name");
                            insertData("street", streetCode, streetName, districtCode);
                        }
                    }
                }
            }

            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送 GET 请求
    public static String sendGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }

    // 插入数据到数据库中
    public static void insertData(String tableName, String code, String name, String parentCode) throws Exception {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "INSERT INTO " + tableName + " (code, name, parent_code) VALUES ('" + code + "', '" + name + "', '" + parentCode + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}
