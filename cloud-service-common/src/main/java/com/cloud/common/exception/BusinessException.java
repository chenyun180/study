package com.cloud.common.exception;

import com.cloud.common.enums.ResultDataEnum;
import com.cloud.common.utils.service.ResultData;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    //返回数据
    private ResultData resultData;

    public BusinessException(ResultDataEnum resultDataEnum) {
        this.resultData=new ResultData(resultDataEnum.code(),resultDataEnum.desc());
    }

    public BusinessException(ResultData resultData) {
        this.resultData=resultData;
    }

    public BusinessException(String code, String message) {
        this.resultData=new ResultData(code, message);
    }

    public BusinessException(String message) {
        this.resultData=new ResultData(ResultDataEnum.FAIL.code(),message);
    }

    public ResultData getResultData() {
        return resultData;
    }
}
