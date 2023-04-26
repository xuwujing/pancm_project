package com.pancm.test.forTest;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Author pancm
 * @Description
 *
 * 使用Java实现一个父级迭代汇总的功能。
 * 有一个List<User>集合，字段有id、parentId和count，下级对象中的parentId为上级对象的id值，其中只有最下级的parentId的count有值，一个上级对象有多个下级对象，通过parentId关联，
 * 示例:
 * 有两条最下级User对象，和一条上级对象，下级对象他们的值分别是: id=101,parentId=10,count=6,
 * id=1002,parentId=10,count=4
 * 上级对象的值是
 * id=11,parentId=1,count=15
 * 那么上级对象的值计算之后为:
 * id=10,parentId=1,count=10,
 * 最上级的的对象值为
 * id=1,parentId=0,count=25
 *
 * 请实现该功能
 * 可以使用Java中的递归算法来实现父级迭代汇总的功能。具体思路如下：
 *
 * （1）将List<User>按parentId进行分组，得到一个Map<Integer,  List<User>>，其中key为parentId，value为属于该parentId的所有User对象；
 *
 * （2）从根节点（parentId为0）开始，递归地遍历所有下级节点，将它们的count值累加到其父级节点的count值中，并返回累加后的结果；
 *
 * （3）将所有节点的结果保存在一个Map<Integer,  Integer>中，其中key为id，value为累加后的count值。
 * @Date  2023/4/25
 * @Param
 * @return
 **/
public class Test2 {
    private int id;
    private int parentId;
    private int count;

    public Test2(int id, int parentId, int count) {
        this.id = id;
        this.parentId = parentId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void main(String[] args) {
        List<Test2> list = new ArrayList<>();
        list.add(new Test2(101, 10, 6));
        list.add(new Test2(102, 10, 4));
        list.add(new Test2(201, 20, 60));
        list.add(new Test2(202, 20, 40));
        list.add(new Test2(20, 1, 0));
        list.add(new Test2(10, 1, 0));
        list.add(new Test2(1, 0, 0));
        Long id = 0L;
        Long name = 1L;
        System.out.println(id!=0);
        System.out.println(id!=1);

        Map<Integer, List<Test2>> userMap = new HashMap<>();
        for (Test2 user : list) {
            int parentId = user.getParentId();
            if (!userMap.containsKey(parentId)) {
                userMap.put(parentId, new ArrayList<>());
            }
            userMap.get(parentId).add(user);
        }

        Map<Integer, Integer> result = new HashMap<>();
        int rootId = 0;
        for (Test2 user : userMap.get(rootId)) {
            sumCount(user, userMap, result);
        }
        System.out.println(result);

    }

    private static int sumCount(Test2 user, Map<Integer, List<Test2>> userMap, Map<Integer, Integer> sumMap) {
        int sum = user.getCount();
        int id = user.getId();
        List<Test2> children = userMap.get(id);
        if (children != null) {
            for (Test2 child : children) {
                sum += sumCount(child, userMap, sumMap);
            }
        }
        sumMap.put(id, sum);
        return sum;
    }
}
