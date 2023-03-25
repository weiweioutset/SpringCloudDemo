package com.cloud.demo.utils;

import java.util.*;

/**
 * @Author weiwei
 * @Date 2022/6/19 下午4:53
 * @Version 1.0
 * @Desc 数字处理工具类
 */
public class NumberUtil {
    /**
     * 获取数字的所有整数位上的数字
     * 例如：123，返回[1] [2] [3]
     * @param number
     * @return
     */
    public static int[] getAllBit(Number number) {
        List<Integer> nums = new ArrayList<>();

        int source = number.intValue();
        while (source > 0) {
            nums.add(source % 10);
            source /= 10;
        }

        Collections.reverse(nums);
        int[] result = new int[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            result[i] = nums.get(i);
        }

        return result;
    }

    /**
     * 判断是否为特殊账号,如连号，重复数字号码
     * 如123456，654321，88888，66666等都属于特殊账号
     * @param number
     * @return
     */
    public static boolean isSpecialNumber(Long number) {
        if (Objects.isNull(number) || number < 10) {
            return false;
        }
        //分离出数字的每一位
        int[] array = getAllBit(number);
        // 判断是否为特殊数字
        boolean flag = true;
        // 是否为重复数字，如888888
        flag = isAllSame(array);
        // 判断是否位正序，如123456
        if (!flag) {
            flag = isPositiveOrder(array);
        }
        // 判断是否位倒序，如654321
        if (!flag) {
            flag = isNegativeOrder(array);
        }

        return flag;
    }

    /**
     * 判断是否为正序，如123456
     * @param nums
     * @return
     */
    public static boolean isPositiveOrder(int[] nums) {
        boolean flag = true;
        for(int i = 1; i< nums.length; i++) {
            int num = nums[i];
            int num_ = nums[i - 1] + 1;
            if (num != num_) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断是否为倒序，如654321
     * @param nums
     * @return
     */
    public static boolean isNegativeOrder(int[] nums) {
        boolean flag = true;
        for(int i = 1; i< nums.length; i++) {
            int num = nums[i];
            int num_ = nums[i - 1] - 1;
            if (num != num_) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 是否为重复数字，如888888
     * @param nums
     * @return
     */
    public static boolean isAllSame(int[] nums) {
        if (nums.length <= 1) {
            return true;
        }
        int number = nums[0];
        for (int num : nums) {
            if (num != number) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        double x = 1670239.8963;

        int[] nums = getAllBit(x);
        System.out.println(Arrays.toString(nums));
    }
}
