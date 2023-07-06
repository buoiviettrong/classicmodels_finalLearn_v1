package com.nixagh.classicmodels._ATestNewContent;

public class FindMedianSortedArrays {
    public static void main(String[] args) {
        int[] nums1 = {2};
        int[] nums2 = {3, 4, 5, 6};
        double result = findMedianSortedArrays(nums1, nums2);
        System.out.println(result);
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;

        if (m > n) return findMedianSortedArrays(nums2, nums1);

        int left = 0, right = m;

        while (left <= right) {
            int partLeftArr1 = (left + right) / 2;
            int partLeftArr2 = (m + n + 1) / 2 - partLeftArr1;

            int maxLeftArr1 = partLeftArr1 == 0 ? Integer.MIN_VALUE : nums1[partLeftArr1 - 1];
            int maxLeftArr2 = partLeftArr2 == 0 ? Integer.MIN_VALUE : nums2[partLeftArr2 - 1];

            int minRightArr1 = partLeftArr1 == m ? Integer.MAX_VALUE : nums1[partLeftArr1];
            int minRightArr2 = partLeftArr2 == n ? Integer.MAX_VALUE : nums2[partLeftArr2];

            if (maxLeftArr1 <= minRightArr2 && maxLeftArr2 <= minRightArr1)
                if ((m + n) % 2 == 1)
                    return Math.max(maxLeftArr1, maxLeftArr2);
                else
                    return (Math.max(maxLeftArr1, maxLeftArr2) + Math.min(minRightArr1, minRightArr2)) / 2.0;
            else if (maxLeftArr1 < minRightArr2)
                left = partLeftArr1 + 1;
            else
                right = partLeftArr1 - 1;
        }
        return 0;
    }
}
