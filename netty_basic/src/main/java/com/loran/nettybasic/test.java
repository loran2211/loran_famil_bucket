package com.loran.nettybasic;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * @Author: luol
 * @Date: 2020/11/11 13:29
 * @Description:
 */
public class test {
    public static void main(String[] args) throws Exception {
        postDemo1();
    }

    /**
     * POST请求
     */
    public static void postDemo() {
        try {
            // 请求的地址
            String spec = "https://test-transferfss-core.izuche.com/transferFss-core/v1/callback/billResult.json";
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置请求方法
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 传递的数据
            String param = "<INFO><IF_BH>废止类型%</IF_BH><RESULT><RTNCODE>F</RTNCODE><DATAID>CLFYBXD2020110300007</DATAID><BILLCODE>CB7832287562011030003</BILLCODE><RTNMSG>1</RTNMSG><EVENTNAME>工作流驳回</EVENTNAME><BillState>30</BillState></RESULT></INFO>";
            String data = "returnMsg=" + URLEncoder.encode(param);
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            // 发送POST请求必须设置允许输出
            urlConnection.setDoOutput(true);
            // 发送POST请求必须设置允许输入
            urlConnection.setDoInput(true);
            //setDoInput的默认值就是true
            //获取输出流,将参数写入
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte[] buffer = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                byteArrayOutputStream.close();
                // 返回字符串
                String result = new String(byteArrayOutputStream.toByteArray());
                System.out.println(result);

            } else {
                System.out.println("请求失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postDemo1() throws Exception {
        String url = "https://test-transferfss-core.izuche.com/transferFss-core/v1/callback/billResult.json";
        URL realUrl = new URL(url);
        // 打开与URL之间的链接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)\"");
        conn.setRequestProperty("Charset", "UTF-8");
        //发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        // 设置请求属性
        String param = "<INFO><IF_BH>废止类型%</IF_BH><RESULT><RTNCODE>F</RTNCODE><DATAID>CLFYBXD2020110300007</DATAID><BILLCODE>CB7832287562011030003</BILLCODE><RTNMSG>1</RTNMSG><EVENTNAME>工作流驳回</EVENTNAME><BillState>30</BillState></RESULT></INFO>";
        String data = "returnMsg=" + URLEncoder.encode(param);
        // 发送请求参数
        out.print(data);
        // flush输出流缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        System.out.print(result);
    }

}
