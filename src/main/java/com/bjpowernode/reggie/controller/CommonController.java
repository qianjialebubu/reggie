package com.bjpowernode.reggie.controller;


import com.bjpowernode.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequestMapping("/common")
@RestController

public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")

    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String s = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID()+s;
//        创建一个目录结构判断
        File dir = new File(basePath);
        if (! dir.exists()){
            dir.mkdirs();
        }



        try {
//            file.transferTo(new File(basePath+"hello.jpg"));
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Uploading");
        System.out.println(9999);
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info("-----------------------------------------------------------");
        System.out.println("----------------------------------------------------------");
//        输入流读取文件内容，输出流输出文件显示到浏览器上
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            System.out.println("_________________________________"+basePath+name+"-----------------");
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
