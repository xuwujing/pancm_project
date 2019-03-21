package com.pancm.test.hbaseTest.others.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Put;

/**
 * 扩展 Put 类
 * Created by babylon on 2016/12/5.
 */
public class SocPut extends Put{

    private Map<String, byte[]> map;
    private List<Map<String, byte[]>> data = new LinkedList<>();

    /**
     * 初始化方法
     *
     * @param row rowKey 名称
     */
    public SocPut(byte[] row) {
        super(row);
    }

    @Override
    public Put addColumn(byte[] family, byte[] qualifier, byte[] value) {
        map = new HashMap<>();
        map.put("family", family);
        map.put("qualifier", qualifier);
        map.put("value", value);
        data.add(map);
        return super.addColumn(family, qualifier, super.ts, value);
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<Map<String, byte[]>> getData() {
        return data;
    }

    /**
     * Get data size int.
     *
     * @return the int
     */
    public int getDataSize(){
        return data.size();
    }

}
