package com.pancm.test.map;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AmapUtil {

    private static final String AMAP_API_KEY = "44cfa7574786e7f35a7f58b545f57819";

    public static String getAddress(double longitude, double latitude) {
        String url = String.format("https://restapi.amap.com/v3/geocode/regeo?key=%s&location=%f,%f&radius=1000&extensions=all&batch=false&roadlevel=0", AMAP_API_KEY, longitude, latitude);

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonStr = response.toString();
            JSONObject jsonObj = JSONObject.parseObject(jsonStr);
            String status = jsonObj.getString("status");
            if ("1".equals(status)) {
                JSONObject regeocode = jsonObj.getJSONObject("regeocode");
                String address = regeocode.getString("formatted_address");
                return address;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        double longitude = 116.397477;
        double latitude = 39.908692;
        String address = AmapUtil.getAddress(longitude, latitude);
        System.out.println(address);



    }
}