package com.hpe.cmca.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.hpe.cmca.util.Json;

/**
 * Servlet user to accept file upload
 */
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String serverPath = "e:/";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       

        System.out.println("进入后台...");

        // 1.创建DiskFileItemFactory对象，配置缓存用
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

        // 2. 创建 ServletFileUpload对象
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

        // 3. 设置文件名称编码
        servletFileUpload.setHeaderEncoding("utf-8");

        // 4. 开始解析文件
        try {
            List<FileItem> items = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : items) {

                if (fileItem.isFormField()) { // >> 普通数据
                    String info = fileItem.getString("utf-8");
                    System.out.println("info:" + info);
                } else { // >> 文件
                    // 1. 获取文件名称
                    String name = fileItem.getName();
                    // 2. 获取文件的实际内容
                    InputStream is = fileItem.getInputStream();

                    // 3. 保存文件
                    FileUtils.copyInputStreamToFile(is, new File(serverPath + "/" + name));
                }

            }
            Map<String,Object> data =new HashMap<String,Object>();
            data.put("status", "success");
            PrintWriter out =response.getWriter();
            out = response.getWriter(); 
            out.write(Json.Encode(data)); 
            out.flush(); 
//            response.getWriter().append("Served at: ").append(request.getContextPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
