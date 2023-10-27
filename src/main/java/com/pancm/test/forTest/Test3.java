package com.pancm.test.forTest;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: 使用Java实现一个父级迭代汇总的功能。
 * 有一个List<User>集合，有N个级别,字段有id、parentId和count，下级对象中的parentId为上级对象的id值，其中只有最下级的parentId的count有值，一个上级对象有多个下级对象，通过parentId关联，并且他们的id顺序不是固定的，降序迭代递归处理
 * 示例:
 * 有两条最下级User对象，和一条上级对象，最下级对象他们的值分别是: id=1001,parentId=100,count=6,
 * id=1002,parentId=100,count=4
 * 下级对象的值是
 * id=100,parentId=10,count=0,
 * 上级对象的值是
 * id=11,parentId=1,count=15
 * 那么上级对象的值计算之后为:
 * id=10,parentId=1,count=10,
 * 最上级的的对象值为
 * id=1,parentId=0,count=25
 * <p>
 * 请实现该功能
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2023/4/25
 */
public class Test3 {

    private Long id;
    private Long parentId;
    private Long count;

    public Test3(Long id, Long parentId, Long count) {
        this.id = id;
        this.parentId = parentId;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
    
    public static void main(String[] args) {
        List<Test3> list = new ArrayList<>();
        list.add(new Test3(1L, 0L, 0L));
        list.add(new Test3(1002L, 101L, 4L));
        list.add(new Test3(201L, 20L, 60L));
        list.add(new Test3(1001L, 101L, 6L));
        list.add(new Test3(101L, 10L, 0L));
        list.add(new Test3(102L, 10L, 0L));
        list.add(new Test3(202L, 20L, 40L));
        list.add(new Test3(2002L, 102L, 20L));
        list.add(new Test3(20L, 1L, 0L));
        list.add(new Test3(10L, 1L, 0L));
//        Collections.sort(list,  Comparator.comparing(Test3::getId));
//        Collections.reverse(list);
        Map<Long, List<Test3>> groupedTest3s = list.stream()
                .collect(Collectors.groupingBy(Test3::getParentId));
        calculateCount(groupedTest3s, 0L);
        for (Test3 test2 : list) {
            Long id = test2.getId();
            Long parentId = test2.getParentId();
            List<Test3> test2s = groupedTest3s.get(parentId);
            if(test2s != null &&test2s.size()>0){
                for (Test3 test21 : test2s) {
                   if(id == test21.getId()) {
                       test2.setCount(test21.getCount());
                   }
                }
            }
        }

        System.out.println("===  "+list);
        System.out.println(groupedTest3s);
        Test3 rootTest3 = groupedTest3s.get(0L).get(0);
        System.out.println("最上级的对象值为：id=" + rootTest3.getId() + ", parentId=" + rootTest3.getParentId() + ", count=" + rootTest3.getCount());

    }

    public static Long calculateCount(Map<Long, List<Test3>> groupedTest3s, Long parentId) {
        List<Test3> children = groupedTest3s.get(parentId);
        if (children == null) { // 如果该节点没有子节点，直接返回0
            return 0L;
        }

        Long sum = 0L;
        for (Test3 child : children) {
            if (child.getCount() != 0) { // 如果是最底层节点，直接加上count值
                sum += child.getCount();
            } else { // 如果是父级节点，递归计算它所有下级对象的count值
                Long childCount = calculateCount(groupedTest3s, child.getId());
                child.setCount(childCount); // 将计算出的count设置到子节点中
                sum += childCount;
            }
        }

        return sum;
    }
}
