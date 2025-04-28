package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Related operation interfaces for files
 */
@RestController
@RequestMapping("/files")
public class FileController {

    // Indicate the storage path of local disk files
    private static final String filePath = System.getProperty("user.dir") + "/files/";

    @Value("${fileBaseUrl}")
    private String fileBaseUrl;

    /**
     * upload files
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // Unique identifier for defining files
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        // Splicing together the complete file storage path
        String realFilePath = filePath + fileName;
        try {
            if (!FileUtil.isDirectory(filePath)) {
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), realFilePath);
        } catch (IOException e) {
            System.out.println("File upload error");
        }
        String url = fileBaseUrl + "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * download files
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        // Set the HTTP response header for downloading files
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        // Splicing together the complete file storage path
        String realFilePath = filePath + fileName;
        try {
            // Retrieve the byte array of the file through its storage path
            byte[] bytes = FileUtil.readBytes(realFilePath);
            ServletOutputStream os = response.getOutputStream();
            // Write the byte array of the file to the file stream
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            System.out.println("File download error");
        }
    }

}
