package com.pancm.test.map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pancm.util.MyTools;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;


public class GetChinaDistricts {
    public static void main(String[] args) {
        String key = "44cfa7574786e7f35a7f58b545f57819";
        String url = "https://restapi.amap.com/v3/config/district?key=" + key + "&subdistrict=3";
        String jsonStr = "";

        try {
            // 从API接口获取JSON数据
            URL requestUrl = new URL(url);
            Scanner scanner = new Scanner(requestUrl.openStream(), "UTF-8");
            while (scanner.hasNext()) {
                jsonStr += scanner.nextLine();
            }
            scanner.close();

            // 解析JSON数据并提取省市区信息
            JSONObject jsonObj = MyTools.toJson(jsonStr);
            JSONArray districts = jsonObj.getJSONArray("districts").getJSONObject(0).getJSONArray("districts");

            // 将省市区信息转化为JSON格式
            JSONObject result = new JSONObject();
            for (int i = 0; i < districts.size(); i++) {
                JSONObject district = districts.getJSONObject(i);
                String districtName = district.getString("name");
                JSONArray cityArray = district.getJSONArray("districts");

                JSONObject districtObj = new JSONObject();
                for (int j = 0; j < cityArray.size(); j++) {
                    JSONObject city = cityArray.getJSONObject(j);
                    String cityName = city.getString("name");
                    JSONArray areaArray = city.getJSONArray("districts");

                    JSONObject cityObj = new JSONObject();
                    for (int k = 0; k < areaArray.size(); k++) {
                        JSONObject area = areaArray.getJSONObject(k);
                        String areaName = area.getString("name");
                        cityObj.put(areaName, "");
                    }
                    districtObj.put(cityName, cityObj);
                }
                result.put(districtName, districtObj);
            }

            // 将结果输出为JSON文件
            FileWriter fileWriter = new FileWriter("china_districts.json");
            fileWriter.write(jsonStr);
            fileWriter.close();
            FileWriter fileWriter2 = new FileWriter("china_districts2.json");
            fileWriter2.write(result.toString());
            fileWriter2.close();

            System.out.println("中国省市区地址信息已成功生成为JSON文件！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}