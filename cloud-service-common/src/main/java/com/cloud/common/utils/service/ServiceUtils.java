package com.cloud.common.utils.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.enums.ResultDataEnum;
import com.cloud.common.enums.StringEnum;
import com.cloud.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServiceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUtils.class);

    //--------------------------通用返回方法------------------------------------
    /**
     * 通用成功返回方法
     * @param data 返回的数据
     */
    public static <T> ResultData<T> callbackSuccess(T data) {
        return new ResultData<T>(data);
    }

    /**
     * 通用失败返回方法
     * @Param message 失败描述
     */
    public static <T> ResultData<T> callbackFail(String message) {
        return new ResultData<T>(ResultDataEnum.FAIL.code(), message, null);
    }

    /**
     * 返回枚举信息
     * @param stringEnum 返回数据状态的枚举
     * @param data           需返回的数据对象
     * @return 统一返回数据包装类型ResultData
     */
    public static <T> ResultData<T> callback(StringEnum stringEnum, T data) {
        return new ResultData<T>(stringEnum.code(), stringEnum.desc(), data);
    }

    /**
     * 异常返回信息
     */
    public static <T> ResultData<T> callback(StringEnum stringEnum) {
        return callback(stringEnum, null);
    }

    /**
     * 自定义异常码，异常信息返回
     */
    public static <T> ResultData<T> callback(String code, String msg) {
        return new ResultData<T>(code, msg, null);
    }


    //---------------------------------抛出异常相关----------------------------------
    /**
     * 抛出自定义异常
     * 注意：当需要记录错误日志，并且需要通知相关人员时，要调用此方法抛出异常。
     *
     * @param resultDataEnum 抛出数据状态的枚举，最终用户会看到此信息
     */
    public static void throwBusinessException(ResultDataEnum resultDataEnum) {
        throw new BusinessException(resultDataEnum);
    }

    /**
     * 抛出自定义异常
     * 注意：当需要记录错误日志，并且需要通知相关人员时，要调用此方法抛出异常。
     *
     * @param resultData 抛出数据，最终用户会看到此信息
     */
    public static void throwBusinessException(ResultData resultData) {
        throw new BusinessException(resultData);
    }

    /**
     * 抛出自定义异常
     * 注意：当需要记录错误日志，并且需要通知相关人员时，要调用此方法抛出异常。
     *
     * @param message 抛出错误的数据，给用户提示信息的为message
     */
    public static void throwBusinessException(String message) {
        throw new BusinessException(message);
    }

    public static void throwBusinessException(String code, String message) {
        throw new BusinessException(code, message);
    }



    //---------------------------远程调用对象转换------------------------------
    /**
     * 转成object
     */
    public static <T> T parseObject(String resultDataJsonSting, Class<T> clazz) {
        ResultData resultData = JSONObject.parseObject(resultDataJsonSting, ResultData.class);
        if (!resultData.isSuccess()) {
            LOGGER.error("接口访问失败......errMsg={}", resultDataJsonSting);
            throwBusinessException(resultData.getMsg());
        }
        return JSONObject.parseObject(resultDataJsonSting).getObject("data", clazz);
    }

    /**
     * 转成List
     */
    public static <T> List<T> parseList(String resultDataJsonSting, Class<T> clazz) {
        ResultData resultData = JSONObject.parseObject(resultDataJsonSting, ResultData.class);
        if (!resultData.isSuccess()) {
            LOGGER.error("接口访问失败....errMsg={}", resultDataJsonSting);
            throwBusinessException(resultData.getMsg());
        }
        return JSONArray.parseArray(String.valueOf(resultData.getData()), clazz);
    }

}
