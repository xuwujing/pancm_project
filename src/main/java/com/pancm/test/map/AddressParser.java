package com.pancm.test.map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pancm.util.MyTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author pancm
 * @Description 获取高德中国的省市区信息并写入到表中
 * 也可以从https://gitcode.net/mirrors/modood/administrative-divisions-of-china/-/blob/master/dist/areas.csv
 * 这里面下载
 * @Date 2023/6/17
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
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("provinceSize:" + provinceArr.size());
            // 遍历省份列表，插入省份数据到数据库中
            for (int i = 0; i < provinceArr.size(); i++) {
                JSONObject provinceObj = provinceArr.getJSONObject(i);
                String provinceCode = provinceObj.getString("adcode");
                String provinceName = provinceObj.getString("name");
                String center = provinceObj.getString("center");
                insertData(conn, "gaode_province", provinceCode, provinceName, "CN", center);

                // 遍历城市列表，插入城市数据到数据库中
                JSONArray cityArr = provinceObj.getJSONArray("districts");
                for (int j = 0; j < cityArr.size(); j++) {
                    JSONObject cityObj = cityArr.getJSONObject(j);
                    String cityCode = cityObj.getString("adcode");
                    String cityName = cityObj.getString("name");
                    center = cityObj.getString("center");
                    insertData(conn, "gaode_city", cityCode, cityName, provinceCode, center);

                    // 遍历区县列表，插入区县数据到数据库中
                    JSONArray districtArr2 = cityObj.getJSONArray("districts");
                    for (int k = 0; k < districtArr2.size(); k++) {
                        JSONObject districtObj = districtArr2.getJSONObject(k);
                        String districtCode = districtObj.getString("adcode");
                        String districtName = districtObj.getString("name");
                        center = districtObj.getString("center");
                        insertData(conn, "gaode_district", districtCode, districtName, cityCode, center);

                        // 遍历街道列表，插入街道数据到数据库中
                        JSONArray streetArr = districtObj.getJSONArray("districts");
                        for (int l = 0; l < streetArr.size(); l++) {
                            JSONObject streetObj = streetArr.getJSONObject(l);
                            String streetCode = streetObj.getString("adcode");
                            String streetName = streetObj.getString("name");
                            center = streetObj.getString("center");
                            insertData(conn, "gaode_street", streetCode, streetName, districtCode, center);
                        }
                    }

                }
            }
            conn.close();

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
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }

    // 插入数据到数据库中
    public static void insertData(Connection conn, String tableName, String code, String name, String parentCode, String center) throws Exception {
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "INSERT INTO " + tableName + " (code, name, parent_code,center) VALUES ('" + code + "', '" + name + "', '" + parentCode + "', '" + center + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
        }
    }
}
