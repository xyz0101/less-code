package com.jenkin.sometools.office.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：jenkin
 * @date ：Created at 2021/6/21 16:04
 * @menu
 * @description：
 * @modified By：
 * @version: 1.0
 */
@RestController
@RequestMapping("/office")
@CrossOrigin
@Api(tags = "office转换相关服务")
public class OfficeController {

    @PostMapping("/convertPdfToWord")
    public void convertPdfToWord(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        ParsePdfToWord parsePdfToWord = new ParsePdfToWord();


        String replace = file.getName().replace(".pdf", ".docx");
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + replace);
        parsePdfToWord.parseFileToWord(file.getInputStream(),response.getOutputStream());
    }
}
