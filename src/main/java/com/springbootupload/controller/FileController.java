package com.springbootupload.controller;

import com.springbootupload.util.FileUtil;
import com.springbootupload.util.JsonData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 功能描述：文件测试
 */
@Controller
public class FileController {
    @Value("${web.upload-path}")
    private String filePath;
    @Value("${web.domain}")
    private String domain;

    @RequestMapping(value = "/upload")
    @ResponseBody
    public JsonData upload(@RequestParam("head_img") MultipartFile[] files, HttpServletRequest request) {
        if (0 == files.length) {
            return new JsonData(5000, null, "fail to file is empty");
        }
        String folder_name = request.getParameter("folder_name");
        folder_name = folder_name.replace(".", "/");
        String formatPath = filePath;
        //E:/Project-resources/
        if (null != folder_name && !folder_name.isEmpty()) {
            formatPath = String.format("%s%s/", filePath, folder_name);
            //E:/Project-resources/images
        }
        FileUtil fileUtil = new FileUtil();
        if (!fileUtil.mkdirsDirectory(formatPath)) {
            return new JsonData(5000, null, "fail to create directory");
        }
        List<String> data = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getOriginalFilename();//上传的文件名
            String suffixName = filename.substring(filename.lastIndexOf("."));//上传的后缀名
            filename = UUID.randomUUID().toString().replace("-", "") + suffixName;//生成后的文件名
            File dest = new File(formatPath + filename);//文件上传的目的地

            try {
                files[i].transferTo(dest);
                if (null != folder_name && !folder_name.isEmpty()) {
                    //http://localhost:8080/images/1b221abf14db4420ad3451d2f139d907.JPG
                    data.add(i, String.format("%s/%s/%s", domain, folder_name, filename));
                } else {
                    //http://localhost:8080/1b221abf14db4420ad3451d2f139d907.JPG
                    data.add(i, String.format("%s/%s", domain, filename));
                }
            } catch (IOException e) {
                return new JsonData(5000, null, e.getMessage());
            }
        }
        return new JsonData(2000, data);
    }


}
