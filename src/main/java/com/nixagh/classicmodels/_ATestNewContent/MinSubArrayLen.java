package com.nixagh.classicmodels._ATestNewContent;

public class MinSubArrayLen {
    public static void main(String[] args) {
        int[] nums = {2, 3, 1, 2, 4, 3};
        int target = 7;
        int result = minSubArrayLen(target, nums);

        // q: the complexity of this algorithm?
        // a: O(nlogn)
    }

    public static int minSubArrayLen(int target, int[] nums) {
        // using binary search instead of two pointers
        int left = 0;
        int right = nums.length;
        int result = 0;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (windowExist(mid, target, nums)) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    public static boolean windowExist(int windowSize, int target, int[] nums) {
        int sum = 0;
        for (int i = 0; i < windowSize; i++) {
            sum += nums[i];
        }

        if (sum >= target) {
            return true;
        }

        for (int i = windowSize; i < nums.length; i++) {
            sum += nums[i] - nums[i - windowSize];
            if (sum >= target) {
                return true;
            }
        }

        return false;
    }

}
