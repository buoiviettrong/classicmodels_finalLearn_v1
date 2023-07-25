package com.nixagh.classicmodels._ATestNewContent;

public class LongestSubarray {
    public static void main(String[] args) {
        int[] nums = new int[]{1, 1, 0, 0, 1, 1, 1, 0, 1};
        System.out.println(longestSubarray(nums));
    }

    private static int longestSubarray(int[] nums) {
        int len = nums.length, count = 0, max = 0, last = 0, flag = 1;

        for (int index = 0; index < len; index++) {
            if (nums[index] == 1) {
                count++;
                continue;
            }
            flag = 0;
            max = Math.max(last + count, max);
            last = count;
            count = 0;
        }
        return Math.max(last + count, max) - flag;
    }
}
