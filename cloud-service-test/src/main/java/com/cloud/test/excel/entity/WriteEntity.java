package com.cloud.test.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.OnceAbsoluteMerge;
import lombok.Data;

import java.util.Date;

/**
 * easyExcel 导出实体
 */
@Data
@ContentRowHeight(15)
@HeadRowHeight(20)
//@ColumnWidth(25)
public class WriteEntity {

    /**
     * 覆盖全局
     */
//    @ColumnWidth(50)
    @ExcelProperty("字符串1标题1")
    private String string;

    @ExcelProperty("日期标题")
    private Date date;

    @ExcelProperty("数字标题")
    private Double doubleData;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;

}
