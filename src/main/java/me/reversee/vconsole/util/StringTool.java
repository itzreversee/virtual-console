package me.reversee.vconsole.util;

public class StringTool {
    public static String removeFirstChar(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.deleteCharAt(0);

        return sb.toString();
    }

    public static String removeLastChar(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.deleteCharAt(s.length() - 1);

        return sb.toString();
    }

    public static String removeCharAt(String s, Integer index) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.deleteCharAt(index);

        return sb.toString();
    }

    public static String removeLastXChars(String s, Integer i) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);

        while (!(i <=0)) {
            i--;
            sb.deleteCharAt(s.length() - 1);
        }

        return sb.toString();
    }

    public static String removeFirstXChars(String s, Integer i) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);

        while (!(i <=0)) {
            i--;
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }

    public static String removeLastCharsUntil(String s, String cu) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);

        while (!(sb.toString().endsWith(cu))) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    public static String removeFirstCharsUntil(String s, String cu) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);

        while (!(sb.toString().endsWith(cu))) {
            sb.deleteCharAt(0);
        }

        return sb.toString();
    }
    
    public static String getQuote(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        while (!(sb.charAt(0) == '\"')) {
            sb.deleteCharAt(0);
        }
        while (!(sb.charAt(sb.length() - 1) == '\"')) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();

    }

}
