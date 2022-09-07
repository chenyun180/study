package com.cloud.common.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 匹配工具类
 */
public class MatchCheckUtil {


    /**
     * 验证字符串是否为数字或字母
     */
    public static boolean isLetterAndNum( String str ) {
        return match("[A-Za-z0-9]*", str);
    }

    /**
     * 验证字符串是否为数字
     */
    public static boolean isNumber( String str ) {
        return match("[0-9]*", str);
    }


    /**
     * 验证邮箱格式是否正确
     */
    public static boolean isEmail( String str ) {
        return match(
                "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$",
                str);
    }


    /**
     * 验证字符串是否为手机号码
     */
    public static boolean isMobile( String str ) {
       // return match("^(1[0-9])\\d{9}$", str);
        return match("^1([358][0-9]|4[579]|66|7[01235678]|9[189])[0-9]{8}$",str);
    }


    /**
     * 正则表达式匹配
     * @param regex 正则表达式字符串
     * @param str 匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    public static boolean match( String regex, String str ) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    /**
     * 验证字符串是否匹配yyyy/MM/dd
     */
    public static boolean matchDate( String date ) {
        String eL = "[0-9]{4}/[0-9]{2}/[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        boolean dateFlag = m.matches();
        return dateFlag;
    }


    /**
     * 验证字符串是否匹配yyyy-MM-dd
     */
    public static boolean matchyyyyMMddDate( String date ) {
        String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(date);
        boolean dateFlag = m.matches();
        return dateFlag;
    }

    /**
     * 验证姓名
     * @param name 待校验参数
     * @return boolean
     */
    public static boolean matchName( String name ) {
        String eL = "[\u4E00-\u9FA5]{2,15}(?:·[\u4E00-\u9FA5]{2,15})*";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(name);
        boolean dateFlag = m.matches();
        return dateFlag;
    }

    /**
     *  验证是否是身份证
     * @param idCard 待校验的身份证
     * @return boolean
     */
    public static boolean matchIdCard( String idCard ) {
        String regex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        return match(regex, idCard);
    }

    /**
     * 校验银行卡号
     * @param bankCard 银行卡号
     * @return boolean
     */
    public static boolean matchBankCard( String bankCard ) {
        String regex = "^[0-9]{19}$";
        return match(regex, bankCard);
    }


    // 验证电话号码
    public static boolean mathPhone( String phoneNum ) {
        String regex = "([0-9]{3,4}-)?[0-9]{7,8}";
        return match(regex, phoneNum);
    }


    public static void main( String[] args ) {

    }
}
