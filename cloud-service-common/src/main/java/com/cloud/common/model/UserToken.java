package com.cloud.common.model;

import java.util.Date;

/**
 * 用户登录成功信息
 */
public class UserToken {
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户登陆手机号
     */
    private String mobile;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户性别，0男、1女，详见枚举类型
     */
    private Integer gender;
    /**
     * 用户头像URL
     */
    private String avatarUrl;
    /**
     * 用户类型:1雇佣维修师、2加盟维修师、3销售、4消费者，详见枚举类型
     */
    private Integer type;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 用户登录的唯一标识
     */
    private String token;

    /**
     * 绑定手机号状态
     */
    private Boolean mobileBindStatus;
    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域编码
     */
    private String areaCode;

    /**
     * 区域名称
     */
    private String areaName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getMobileBindStatus() {
        return mobileBindStatus;
    }

    public void setMobileBindStatus(Boolean mobileBindStatus) {
        this.mobileBindStatus = mobileBindStatus;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
