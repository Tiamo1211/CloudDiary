package com.dxc.note.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 将对象转换成JOSN格式的字符串，响应给ajax的回调函数
 * @author ding
 * @data 2021/5/9 -21:03
 */
public class JsonUtil {

    public static void toJson(HttpServletResponse response, Object result) {
        try {
            // 设置响应类型及编码格式 （json类型）
            response.setContentType("application/json;charset=UTF-8");
            // 得到字符输出流
            PrintWriter out = response.getWriter();
            // 通过fastjson的方法，将ResultInfo对象转换成JSON格式的字符串
            String json = JSON.toJSONString(result);
            // 通过输出流输出JSON格式的字符串
            out.write(json);
            // 关闭资源
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
