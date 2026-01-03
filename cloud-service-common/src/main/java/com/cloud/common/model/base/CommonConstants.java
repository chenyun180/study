package com.cloud.common.model.base;

/**
 * 常量
 *
 * @author lm
 * @date 2019/4/19 21:38
 * @Description TODO
 **/
public interface CommonConstants {

    /**
     * 验证码3天
     **/
    long TIME_OUT = 259200;
    /**
     * 电费工单缓存过期时间30天
     **/
    long THIRDPART_TIME_OUT = 30 * 24 * 3600;

    /**
     * 前端工程名
     **/
    String FRONT_END_PROJECT = "nies_ui";

    /**
     * 后端工程名
     **/
    String BACK_END_PROJECT = "nies_niescloud";

    /**
     * 正常
     **/
    Integer STATUS_NORMAL = 0;
    /**
     * 不正常
     */
    Integer STATUS_UNNORMAL = 1;

    /**
     * 越权
     */
    Integer STATUS_UNAUTHORIZED = 2;
    /**
     * 锁定
     **/
    Integer STATUS_LOCK = 9;

    /**
     * 成功标记
     **/
    Integer SUCCESS = 0;
    /**
     * 菜单
     **/
    Integer MENU = 1;

    /**
     * 失败标记
     **/
    Integer FAIL = 1;

    /**
     * 验证码前缀
     **/
    String CAPTCH_CODE_KEY = "CAPTCH_CODE_KEY_";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * 默认当前页码
     */
    Integer CURRENT_PAGE = 1;

    /**
     * 默认当前页大小
     */
    Integer PAGE_SIZE = 20;

    String DWP = "password";

    /**
     * 股友
     */
    String DEFAULT_DWP = "1QAZ!qaz!@#$";

    /**
     * 验证码
     */
    String DEFEULT_CODE = "975521";
}
