package com.nixagh.classicmodels._ATestNewContent;

public class TwoSum {
    public static void main(String[] args) {
        int[] nums = {3, 2, 4};
        int target = 6;
        int[] result = twoSum(nums, target);
    }

    public static int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        int left = 0;
        int right = 1;
        int len = nums.length;

        while (left < len) {
            if (nums[left] + nums[right] == target) {
                result[0] = left;
                result[1] = right;
                break;
            }
            right += 1;
            if (right >= len) {
                left++;
                right = left + 1;
            }
        }
        return result;
    }
}
