package com.nixagh.classicmodels._ATestNewContent;

public class ValidIPv4Address {
    public static void main(String[] args) {
        String input = "221.108.163.44";
        String[] data = input.split("\\.");
        boolean flg = true;
        if (data.length != 4) {
            flg = false;
        } else
            for (int i = 0; i < 4; i++)
                if (Integer.parseInt(data[i]) < 0 || Integer.parseInt(data[i]) > 255) {
                    flg = false;
                    break;
                }
        if (flg) System.out.println("Valid");
        else System.out.println("Invalid");
    }
}
