package com.cloud.common.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 驼峰下划线互转
 */
public class CamelCaseUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     */
    public static String camelToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String[] camelToLineForArray(String[] strArray){
        if(strArray==null || strArray.length<1){
            return strArray;
        }
        for (int i=0;i<strArray.length;i++){
            strArray[i]=camelToLine(strArray[i]);
        }
        return strArray;
    }


}
