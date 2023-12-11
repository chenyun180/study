package com.cloud.common.utils.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.common.enums.ResultDataEnum;
import com.cloud.common.model.BasePage;
import com.cloud.common.utils.HttpUtil;
import com.cloud.common.utils.common.CamelCaseUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 控制器基类
 */
public class BaseController {

    @Resource
    protected HttpServletRequest request;

    // --------------------------接口返回相关-------------------------------
    /**
     * 自定义对象中日期格式，成功通用返回方法。
     * 日期格式默认为yyyy-MM-dd HH:mm:ss
     *
     * @param obj        需要返回的数据对象
     * @param dateFormat 日期格式（例如：yyyy-MM-dd）
     * @return
     */
    protected String callbackWithDateFormat(Object obj, String dateFormat) {
        if (!(obj instanceof ResultData)) {
            obj = ServiceUtils.callbackSuccess(obj);
        }
        if (StringUtils.isBlank(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        return JSON.toJSONStringWithDateFormat(obj, dateFormat, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteMapNullValue
        );
    }

    /**
     * 成功通用返回方法
     *
     * @param obj 需要返回的数据对象
     * @return obj一般为ServiceUtils工具类返回的信息
     */
    protected String callbackSuccess(Object obj) {
        return callbackWithDateFormat(obj, null);
    }

    /**
     * 失败通用返回方法
     *
     * @param message 操作失败的描述信息
     * @return 格式化为JOSN 后的字符串
     */
    protected String callbackFail(String message) {
        return callbackWithDateFormat(ServiceUtils.callbackFail(message), null);
    }



    // ----------------------------自定义异常相关-----------------------------------
    /**
     * 抛出自定义异常
     * 注意：当需要记录错误日志，并且需要通知相关人员时，要调用此方法抛出异常。
     *
     * @param resultDataEnum 抛出数据状态的枚举，最终用户会看到此信息
     */
    protected void throwBusinessException(ResultDataEnum resultDataEnum) {
        ServiceUtils.throwBusinessException(resultDataEnum);
    }

    /**
     * 抛出自定义异常
     * 注意：当需要记录错误日志，并且需要通知相关人员时，要调用此方法抛出异常。
     *
     * @param message 抛出错误的数据，给用户提示信息的为message
     */
    protected void throwBusinessException(String message) {
        ServiceUtils.throwBusinessException(message);
    }

    /**
     * 自定义code，message
     */
    protected void throwBusinessException(String code, String message) {
        ServiceUtils.throwBusinessException(code, message);
    }


    //-----------------------mybatis-plus 分页 相关------------------------

    /**
     * 获取分页对象(@RequestBody 接收的不要调用)
     */
    protected <T> Page<T> getPage(int size) {
        Page<T> page = new Page<>(HttpUtil.getInt(request, "pageNo", 1), HttpUtil.getInt(request, "pageSize", size));
        String sortOrder = request.getParameter("sortOrder");
        String sortField = request.getParameter("sortField");
        if(ObjectUtils.allNotNull(sortOrder,sortField)){
            //ascend 或者 descend
            if("ascend".equals(sortOrder)){
                page.setAsc(sortField);
            }else if("descend".equals(sortOrder)){
                page.setDesc(sortField);
            }
        }
        return page;
    }

}
