package com.example.foreign.exchange.common.util;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * EasyExcel导出工具类
 */
public class EasyExcelUtil {
    
    /**
     * 导出Excel
     * @param response HttpServletResponse
     * @param dataList 数据列表
     * @param fileName 文件名
     * @param clazz 数据类型
     * @param <T> 泛型
     * @throws IOException 异常
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> dataList, String fileName, Class<T> clazz) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 防止中文乱码
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        // 导出Excel
        OutputStream outputStream = response.getOutputStream();
        EasyExcel.write(outputStream, clazz).sheet("导出数据").doWrite(dataList);
        outputStream.flush();
        outputStream.close();
    }
}