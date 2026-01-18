package com.cloud.common.model.base;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.model.base.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author lm
 * @date 2019/4/20 12:23
 * @Description TODO
 **/
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@ApiModel(value = "返回结果集", description = "返回结果集")
@Slf4j
public class ResultEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回状态码", name = "code")
    private int code = CommonConstants.SUCCESS;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回提示信息", name = "msg")
    private String msg = "success";


    @Getter
    @Setter
    @ApiModelProperty(value = "返回数据", name = "data")
    private T data;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回机密数据", name = "encrypt_data")
    private String encrypt_data;

    @Getter
    @Setter
    @ApiModelProperty(value = "返回其他数据", name = "other")
    private JSONObject other;

    public ResultEntity() {
        super();
    }


    public ResultEntity(T data, JSONObject other) {
        super();
        this.other = other;
        this.data = data;
    }

    public ResultEntity(T data) {
        super();
        this.data = data;
    }

    public ResultEntity(int code, String msg, T data) {
        super();
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ResultEntity(String msg) {
        super();
        this.msg = msg;
        this.code = CommonConstants.FAIL;
    }

    public ResultEntity(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = CommonConstants.FAIL;
    }

    public ResultEntity(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }


    public static <T> ResultEntity<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, "success");
    }

    public static <T> ResultEntity<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, "success");
    }

    public static <T> ResultEntity<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> ResultEntity<T> failed() {
        return restResult(null, CommonConstants.FAIL, null);
    }

    public static <T> ResultEntity<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg);
    }

    public static <T> ResultEntity<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null);
    }

    public static <T> ResultEntity<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    private static <T> ResultEntity<T> restResult(T data, int code, String msg) {
        ResultEntity<T> apiResult = new ResultEntity<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

}
