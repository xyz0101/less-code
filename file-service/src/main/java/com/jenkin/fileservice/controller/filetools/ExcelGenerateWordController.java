package com.jenkin.fileservice.controller.filetools;

import com.jenkin.common.entity.Response;
import com.jenkin.common.entity.vos.file.ExcelToWordVo;
import com.jenkin.common.entity.vos.file.RenameFileByExcelVo;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.fileservice.service.filetools.ExcelGenerateWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ：jenkin
 * @date ：Created at 2021/6/28 17:33
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Api(tags = "excel生成word")
@RestController
@RequestMapping("/excelword")
@CrossOrigin
public class ExcelGenerateWordController {

    @Autowired
    private ExcelGenerateWordService excelGenerateWordService;

    @Autowired
    private FileService fileService;

    @ApiOperation("根据excel内容生成word文件")
    @PostMapping("/generate")
    public Response<String> generate(@RequestBody ExcelToWordVo excelToWordVo, HttpServletResponse response) throws Exception {
        InputStream word = fileService.getFile(excelToWordVo.getWord());
        InputStream excel = fileService.getFile(excelToWordVo.getExcel());

        String code = excelGenerateWordService.generate(excel, word,
                                          excelToWordVo.getRowStartIndex(),
                                          excelToWordVo.getFileNameCol()  ,
                                          response
                );
        return Response.ok(code);
    }
    @ApiOperation("根据excel内容生成word文件")
    @PostMapping("/renameFileByExcel")
    public Response<String> renameFileByExcel(@RequestBody RenameFileByExcelVo renameFileByExcelVo, HttpServletResponse response) throws Exception {
        InputStream zip = fileService.getFile(renameFileByExcelVo.getZipFile());
        InputStream excel = fileService.getFile(renameFileByExcelVo.getExcel());

        String code = excelGenerateWordService.renameFileByExcel(excel, zip,
                renameFileByExcelVo.getRowStartIndex(),
                renameFileByExcelVo.getFileNameCol()  ,
                response
        );
        return Response.ok(code);
    }
}
