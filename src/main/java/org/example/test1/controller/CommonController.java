package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.test1.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
@Tag(name = "通用接口", description = "文件上传等通用接口")
public class CommonController {

    @Value("${sky.upload.path:upload/}")
    private String basePath;

    @Operation(summary = "文件上传", description = "上传图片文件")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";

        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }

        return Result.success(fileName);
    }

    @Operation(summary = "文件下载", description = "下载图片文件")
    @GetMapping("/download")
    public void download(@RequestParam String name, jakarta.servlet.http.HttpServletResponse response) {
        java.io.FileInputStream fis = null;
        java.io.OutputStream os = null;
        try {
            fis = new java.io.FileInputStream(basePath + name);
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            log.error("文件下载失败", e);
        } finally {
            try {
                if (fis != null) fis.close();
                if (os != null) os.close();
            } catch (IOException e) {
                log.error("关闭流失败", e);
            }
        }
    }
}
