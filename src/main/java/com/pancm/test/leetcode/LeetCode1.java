package com.pancm.test.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pancm
 * @Title: pancm_project
 * @Description: LeetCode
 * 求两数之和
 * 题目地址:
 * @Version:1.0.0
 * @Since:jdk1.8
 * @date 2020/2/17 0017
 */
public class LeetCode1 {

    public static void main(String[] args) {

        /**
         * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 
         *  两个整数，并返回他们的数组下标。
         * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
         *  给定 nums = [2, 7, 11, 15], target = 9
         *  因为 nums[0] + nums[1] = 2 + 7 = 9
         *   所以返回 [0, 1]
         *
         **/

        test1();
    }
    /**
     * @Author pancm
     * @Description
     * @Date  2020/5/18
     * @Param []
     * @return void
     **/
     private static  void test1(){
//        int []nums = {2, 7, 11, 15};
//         int target = 9;
        int []nums2 = {2, 5, 5, 11};
        int target2 = 10;
        int []nums = {1,3,4,2};
        int target = 6;

        int []s =twoSum(nums,target);
        int []s2 =twoSum2(nums,target);
        int []s3 =twoSum2(nums2,target2);
         for (int i : s) {
             System.out.println(i);
         }
         for (int i : s2) {
             System.out.println(i);
         }
         for (int i : s3) {
             System.out.println(i);
         }

      }

    public static int[] twoSum(int[] nums, int target) {
         int []s = new int[2];
        for (int i = 0; i < nums.length; i++) {
            int q= nums[i];
            for (int i1 = nums.length-1; i1 > 0; i1--) {
                int w =nums[i1];
                if((q+w)==target && i!=i1){
                    s[0]=i;
                    s[1]=i1;
                    return s;
                }
            }
        }
         return s;
    }

    /**
     * @Author pancm
     * @Description 利用map处理，但是数组有重复的数据将不行
     * @Date  2020/5/18
     * @Param [nums, target]
     * @return int[]
     **/
    public static int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i< nums.length; i++) {
            if(map.containsKey(target - nums[i])) {
                return new int[] {map.get(target-nums[i]),i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }




}
