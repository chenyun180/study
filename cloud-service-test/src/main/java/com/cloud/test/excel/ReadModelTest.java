package com.cloud.test.excel;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 读取resource目录下的模板（一般是复杂的表头，然后追加内容）
 */
@Slf4j
public class ReadModelTest {

    public static void main(String[] args) {

    }

    public void download() throws Exception {
        //模板路径
        InputStream in = getAbsolutePath("model.xlsx");
        XSSFSheet sheet = null;
        try (XSSFWorkbook xwb = new XSSFWorkbook(in)) {
            sheet = xwb.getSheetAt(0);

            //获取数据并写入Excel
//            exportCommonService.getDataAndBuildSheet(params, sheet, xwb, fileName);

            //将文件上传到指定目录
//            uploadFile(fileName, xwb);


            //将文件上传到MinIO
//            AttachVo attachVo = reportAppendixService.uploadFile(fileName);

            //插入 报表记录表、报表流程跟踪记录表

        } catch (IOException e) {
            log.error("下载Excel模板异常，异常信息为：【{}】", e.getMessage(), e);

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("IOException when close InputStream");
                }
            }
        }

    }

    private InputStream getAbsolutePath(String fileName) {

        String path = "/" + "template" + "/" + fileName;
        InputStream in = this.getClass().getResourceAsStream(path);
        return in;
    }

}
