package com.pancm.test.map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pancm.util.MyTools;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

/**
 * @Author pancm
 * @Description 调用高德地理编码接口获取省市区街道code和名称
 * CREATE TABLE gaode_address (
 * id INT PRIMARY KEY,
 * address VARCHAR(255)
 * );
 * <p>
 * CREATE TABLE gaode_address2 (
 * id INT PRIMARY KEY,
 * province_code VARCHAR(255),
 * province_name VARCHAR(255),
 * city_code VARCHAR(255),
 * city_name VARCHAR(255),
 * district_code VARCHAR(255),
 * district_name VARCHAR(255),
 * street_code VARCHAR(255),
 * street_name VARCHAR(255),
 * center VARCHAR(255)
 * );
 * @Date 2023/9/26
 * @Param
 * @return
 **/
@Slf4j
public class BatchQueryAndGeocoding {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 连接数据库
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcm_demo", "root", "123456");
            stmt = conn.createStatement();

            // 查询gaode_address表的address字段
            String sql = "SELECT  street_code,city_name,formatted_address FROM jz_all_street where  center is null";
            rs = stmt.executeQuery(sql);

            // 遍历查询结果
            while (rs.next()) {
                String streetCode2 = rs.getString("street_code");
                String cityName = rs.getString("city_name");
                String address = rs.getString("formatted_address");
                // 调用高德地理编码接口
                String apiUrl = "https://restapi.amap.com/v3/geocode/geo";
                String apiKey = "9eb6ed2b222cb065a435208fc1e1f374";
                // 构建请求URL
                String requestUrl = apiUrl + "?address=" + address + "?city=" + cityName + "&key=" + apiKey;
                String data = geocoding(requestUrl);
                JSONObject geocodingResult = JSON.parseObject(data);
//                log.info("geocodingResult：" + geocodingResult);
                if (geocodingResult == null || !"1".equals(geocodingResult.getString("status"))) {
                    log.error("geocodingResult:" + geocodingResult);
                    log.error("调用高德地图失败!" + streetCode2);
                    Thread.sleep(10000);
                    data = geocoding(requestUrl);
                    geocodingResult = JSON.parseObject(data);
                    if (geocodingResult == null || !"1".equals(geocodingResult.getString("status"))) {
                        log.error("geocodingResult2:" + geocodingResult);
                        log.error("调用高德地图二次失败!" + streetCode2);
                        continue;
                    }
                }
                JSONObject jsonObject = (JSONObject) geocodingResult.getJSONArray("geocodes").get(0);
                // 解析地理编码结果
                String streetName = jsonObject.getString("street");
                //获取经纬度
                String center = jsonObject.getString("location");
                String tableName = "jz_all_street";
                String updateSql = " update " + tableName + " set center = '" + center + "' where street_code = " + streetCode2;
                executeUpdate(conn, updateSql);
//                //调用逆地址编码
//                String apiUrl2 = "https://restapi.amap.com/v3/geocode/regeo";
//                // 构建请求URL
//                String requestUrl2 = apiUrl2 + "?location=" + center + "&key=" + apiKey;
//                String data2 = geocoding(requestUrl2);
//                log.info("data2" + data);
//                JSONObject geocodingResult2 = JSON.parseObject(data2);
//                JSONObject jsonObject2 = geocodingResult2.getJSONObject("regeocode");
//                JSONObject jsonObject1 = jsonObject2.getJSONObject("addressComponent");
//                String formattedAddress = jsonObject2.getString("formatted_address");
//                String cityCode = jsonObject1.getString("citycode");
//                String districtCode = jsonObject1.getString("adcode");
//                String districtName = jsonObject1.getString("district");
//                String streetCode = jsonObject1.getString("towncode");
//                String provinceName = jsonObject1.getString("towncode");
//                streetName = jsonObject1.getString("township");
//
//                String insertSql = "INSERT INTO gaode_address (create_time, modified_time, creator_id, modifier_id, deleted, province_name, city_code, city_name, district_code, district_name, street_code, street_name, formatted_address, center) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//                PreparedStatement statement = conn.prepareStatement(insertSql);
//                statement.setString(1, MyTools.getNowTime());
//                statement.setString(2, MyTools.getNowTime());
//                statement.setString(3, "admin");
//                statement.setString(4, "admin");
//                statement.setInt(5, 0);
//                statement.setString(6, provinceName);
//                statement.setString(7, cityCode);
//                statement.setString(8, cityName);
//                statement.setString(9, districtCode);
//                statement.setString(10, districtName);
//                statement.setString(11, streetCode);
//                statement.setString(12, streetName);
//                statement.setString(13, formattedAddress);
//                statement.setString(14, center);
//                statement.executeUpdate();
                log.info("写入成功!" + streetName);
            }
        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接和资源
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static int executeUpdate(Connection connection, String sql) throws SQLException {
        Statement stmt = null;
        int i = 0;
        try {
            stmt = connection.createStatement();
            i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return i;
    }

    private static String geocoding(String urlString) {
        try {
            // 发送HTTP GET请求
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 获取响应结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}