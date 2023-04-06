package com.pancm.test.map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2023/4/6
 */
public class GaodeMapTest {


    public static void main(String[] args) {
        try {
            String origin = "广州塔";
            String destination = "广州动物园";
            String city = "广州";
            String key = "44cfa7574786e7f35a7f58b545f57819";
            String urlStr = "https://restapi.amap.com/v3/direction/transit/integrated?origin="
                    + URLEncoder.encode(origin, "UTF-8") + "&destination=" + URLEncoder.encode(destination, "UTF-8")
                    + "&city=" + URLEncoder.encode(city, "UTF-8") + "&key=" + key;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            StringBuilder result = new StringBuilder();
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
