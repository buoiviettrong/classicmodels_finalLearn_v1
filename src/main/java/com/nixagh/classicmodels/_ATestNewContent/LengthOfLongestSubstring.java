package com.nixagh.classicmodels._ATestNewContent;

public class LengthOfLongestSubstring {
    public static void main(String[] args) {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring(s);

        // q: the complexity of this algorithm?
        // a: O(n)
    }

    public static int lengthOfLongestSubstring(String s) {
        int[] map = new int[128];
        int left = 0;
        int right = 0;
        int result = 0;

        while (right < s.length()) {
            char c = s.charAt(right);
            map[c] += 1;
            while (map[c] > 1) {
                char d = s.charAt(left);
                map[d] -= 1;
                left += 1;
            }
            result = Math.max(result, right - left + 1);
            right += 1;
        }
        return result;
    }
}
