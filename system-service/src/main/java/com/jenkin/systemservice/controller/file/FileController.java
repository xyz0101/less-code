package com.jenkin.systemservice.controller.file;

import cn.hutool.core.io.FileUtil;
import com.jenkin.common.entity.Response;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jenkin
 * @className FileController
 * @description TODO
 * @date 2020/12/15 16:01
 */
@RestController("/file")
@Api(tags = "文件操作接口")
public class FileController {

    @Autowired
    private FileService fileService;
    @PostMapping("/uploadFile")
    @ApiOperation(("文件上传"))
    public Response<String> uploadFile(MultipartFile file){
        String code = fileService.uploadFile(file);
        return Response.ok(code);
    }
    @DeleteMapping("/deleteFile")
    @ApiOperation(("文件删除"))
    public Response<String> deleteFile(String code){
        fileService.deleteFile(code);
        return Response.ok();
    }

    @GetMapping("/downloadFile")
    @ApiOperation(("文件下载"))
    public void downloadFile(String code, HttpServletResponse response){
        InputStream file = fileService.getFile(code);

        FileUtils.downloadFile(code,file,response);
    }

}
