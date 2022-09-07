package com.cloud.common.exception;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.cloud.common.enums.ResultDataEnum;
import com.cloud.common.utils.service.ResultData;
import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常捕获
 */
@ControllerAdvice//控制器增强
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultDataEnum> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultDataEnum> builder = ImmutableMap.builder();

    //捕获BusinessException此类异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultData businessException(BusinessException businessException) {
        //记录日志
        LOGGER.error("business catch exception:{}", JSONObject.toJSONString(businessException.getResultData()));
        return businessException.getResultData();
    }

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData exception(Exception exception) {
        exception.printStackTrace();
        //打印日志
        LOGGER.error("catch exception:{}", exception);
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        ResultDataEnum sysExeption = EXCEPTIONS.get(exception.getClass());
        if (sysExeption != null) {
            return new ResultData(sysExeption.code(), sysExeption.desc());
        }
        //未定义的异常统一返回99999异常
        return new ResultData(ResultDataEnum.SERVER_ERROR.code(), ResultDataEnum.SERVER_ERROR.desc());
    }

    static {
        //定义异常类型所对应的错误代码 aa
        builder.put(HttpMessageNotReadableException.class, ResultDataEnum.PARAM_INVALID);
//        builder.put(NoHandlerFoundException.class,ResultDataEnum.PATH_UNFIND);
        builder.put(MissingServletRequestParameterException.class,ResultDataEnum.PARAM_REQUIRED);
        builder.put(JSONException.class,ResultDataEnum.PARAM_JSON);
    }
}
