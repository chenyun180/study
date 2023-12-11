package com.cloud.test.util;

import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JxlsUtil {

    public static void main(String[] args) throws Exception {
        String templatePath = "/Users/cloud/Downloads/USER_MONTH_REPORT.xlsx";
        InputStream is = new FileInputStream(templatePath);
        String path = "/Users/cloud/Downloads/USER_MONTH_REPORT_Result.xlsx";
        OutputStream os = new FileOutputStream(path);
        //excel模板内，数据组装
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("province_name", "111");
        map.put("total", "222");
        map.put("PLANTYPE", "333");
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("province_name", "1111");
        map1.put("total", "2222");
        map1.put("PLANTYPE", "3333");
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList.add(map);
        dataList.add(map1);
        Context context = new Context();

        context.putVar("datas", dataList);
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        jxlsHelper.processTemplate(context, transformer);
    }

}
