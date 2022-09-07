package com.cloud.common.model;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author hutao
 * @Date 2019−03-28 下午 9:09
 * 接收参数基类
 */
public class BaseRequest {

    /**
     * 其他存在更多自定义参数,请在其继承类中定义，也可根据需要在这里扩展参数定义
     * 如接受基类就能满足需要,可以直接使用基类，不必继承了;
     */

    private String name;//名称参数
    private Integer type;//抽象类型参数：具体见继承类的注释

    private UserToken userToken;//用户token，用于设置参数，不用做接收参数[需要在controller中手动设置]

    private Integer pageIndex;//当前页参数    客户端请求服务器使用了post请求 body体application/json,后端使用@requestbody接收,导致reqeust无法获取分页参数，封装到对象中获取
    private Integer pageSize;//每页记录条数参数
    private String orderBy;//排序参数  aa



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPageIndex() {
        if(pageIndex==null){
            return 1;//默认从第1页开始
        }
        return pageIndex;
    }

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        if(pageSize==null){
            return 10;//默认条数10
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        //SQL过滤，防止注入
        String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
                + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
        Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        if(StringUtils.isNotBlank(orderBy)){
            if (sqlPattern.matcher(orderBy).find()) {
                return "";
            }
        }
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
