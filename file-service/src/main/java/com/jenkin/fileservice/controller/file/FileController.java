package com.jenkin.fileservice.controller.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.dtos.file.LscFileDto;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.utils.FileUtils;
import com.jenkin.fileservice.service.file.LscFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;

/**
 * @author jenkin
 * @className FileController
 * @description TODO
 * @date 2020/12/15 16:01
 */
@RestController()
@RequestMapping("/file")
@Api(tags = "文件操作接口")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private LscFileService lscFileService;


    @PostMapping("/uploadFile")
    @ApiOperation(("文件上传"))
    public Response<String> uploadFile(MultipartFile file,HttpServletRequest request){
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
    @PostMapping(value = "/docx/save",
            produces = "application/json;charset=UTF-8")
    public void saveWord(HttpServletRequest request, HttpServletResponse response) {


        String fileId = request.getParameter("fileId");
        int id = -1;
        java.net.HttpURLConnection connection =null;
        PrintWriter writer = null;
        try {
          writer=response.getWriter();
            String body = "";
            id = Integer.parseInt(fileId);
            try {
                Scanner scanner = new Scanner(request.getInputStream());
                scanner.useDelimiter("\\A");
                body = scanner.hasNext() ? scanner.next() : "";
                scanner.close();
            }
            catch (Exception ex) {
                writer.write("get request.getInputStream error:" + ex.getMessage());
                return;
            }
            if (body.isEmpty()) {
                writer.write("empty request.getInputStream");
                return;
            }

            JSONObject jsonObj = JSON.parseObject(body);

            int status = (Integer) jsonObj.get("status");

            int saved = 0;
            if(status == 2 || status == 3) {
                String downloadUri = (String) jsonObj.get("url");
                URL url = new URL(downloadUri);
                connection= (java.net.HttpURLConnection) url.openConnection();
                InputStream stream = connection.getInputStream();
                lscFileService.updateFile(stream,id);
            }
            writer.write("{\"error\":" + saved + "}");
        } catch (Exception e) {
            writer.write("{\"error\":\"-1\"}");
            e.printStackTrace();
        }finally {
            if (connection!=null) {
                connection.disconnect();
            }
        }

    }
}
