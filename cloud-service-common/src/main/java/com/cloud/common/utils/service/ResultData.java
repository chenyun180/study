package com.cloud.common.utils.service;


import com.cloud.common.enums.ResultDataEnum;

/**
 * 用此对象封装后转成json返回结果信息
 */
public class ResultData<T> {
    /**
     * 约定的状态
     */
    private String code;
    /**
     * 失败或成功的提示信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    public ResultData(){
        this(ResultDataEnum.SUCCESS.code(), ResultDataEnum.SUCCESS.desc());
    }
    public ResultData(T data) {
        this(ResultDataEnum.SUCCESS.code(), ResultDataEnum.SUCCESS.desc(), data);
    }

    public ResultData(String code, String msg) {
        this(code, msg, null);
    }

    public ResultData(String code, String msg, T data) {
        if(data instanceof ResultDataEnum){
            this.code=((ResultDataEnum) data).code();
            this.msg=((ResultDataEnum) data).desc();
            this.data = null;
            return;
        }
        if(!(data instanceof  ResultData)){
            this.code = code;
            this.msg = msg;
            this.data = data;
        }
    }

    public boolean isSuccess(){
        if(this.code.equals(ResultDataEnum.SUCCESS.code())){
            return true;
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
