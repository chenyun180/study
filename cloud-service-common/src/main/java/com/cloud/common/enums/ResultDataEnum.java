package com.cloud.common.enums;

/**
 * 给客户端返回数据的枚举
 */
public enum ResultDataEnum implements StringEnum {
    // 系统的未知错误，Exception
    SERVER_ERROR("999999", "请稍后重试"),
    SUCCESS("0000", "成功" ),
    FAIL("9999", "操作失败" ),

    // 以1开头为共性类型
    PARAM_INVALID("1002", "参数无效"),
    PATH_UNFIND("1003", "请求路径不存在，请检查URL"),
    UNAUTHENTICATED("1004","请登录后操作"),
    PARAM_REQUIRED("1005","必须参数不全"),
    PARAM_JSON("1006","参数转换异常"),
    UN_SERVICE("1007","服务暂时不可用,稍等片刻..."),
    AUTH_INVALID("1008", "鉴权失败...");


    /**
     * 枚举码
     */
    private final String code;
    /**
     * 枚举描述
     */
    private final String desc;

    ResultDataEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String desc() {
        return desc;
    }
}
