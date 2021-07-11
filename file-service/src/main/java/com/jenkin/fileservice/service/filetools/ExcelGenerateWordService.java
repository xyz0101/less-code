package com.jenkin.fileservice.service.filetools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import com.deepoove.poi.XWPFTemplate;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.utils.FileUtils;
import com.jenkin.fileservice.util.PoiUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * @author ：jenkin
 * @date ：Created at 2021/6/28 14:38
 * @description： 根据Excel生成word
 * @modified By：
 * @version: 1.0
 */
@Slf4j
@Service
public class ExcelGenerateWordService {
    @Autowired
    private FileService fileService;

    /**
     * 把Excel每一行的信息映射到word模板里面
     *
     * @param excel             excel数据文件
     * @param docx              模板
     * @param contentStartIndex 内容开始索引
     * @param titleColIndex     文件名
     * @param response          响应
     * @throws Exception
     */
    public String generate(InputStream excel, InputStream docx, Integer contentStartIndex,
                           String titleColIndex, HttpServletResponse response) throws Exception {
        String returnCode = null;
        List<File> files = new ArrayList<>();
        ByteArrayInputStream word = null;
//        byte[] bytes = IoUtil.readBytes(docx);
        XWPFTemplate template = XWPFTemplate.compile(docx);
        Map<String, Integer> cache = new HashMap<>();
        try {
//            word = new ByteArrayInputStream(bytes);

            List<Map<String, String>> maps =
                    PoiUtils.readTable(excel, contentStartIndex);
            List<String> paths = new ArrayList<>();


            List<InputStream> streams = new ArrayList<>();
            for (Map<String, String> map : maps) {
                System.out.println(map);
                if (StringUtils.hasLength(map.get("A")) && StringUtils.hasLength(map.get("B"))) {
                    System.out.println("读取模板");
                    String dir = System.getProperty("user.dir");
                    File file = new File(dir + "\\out");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String name = map.get(titleColIndex);
                    Integer ind = cache.getOrDefault(name, 0);
                    if (ind > 0) {
                        name = name + " - " + ind;
                    }
                    cache.put(name, ind + 1);
                    String path = dir + "\\out\\" + name + ".docx";

                    File res = new File(path);
                    if (!res.exists()) {
                        res.createNewFile();
                    }
                    files.add(res);
                    OutputStream outputStream =
                            new FileOutputStream(res);

                    template.render(map);
                    template.write(outputStream);
                    outputStream.close();
                    outputStream.flush();

                    streams.add(new FileInputStream(path));
                    paths.add(name + ".docx");
                }
            }
            File outFile = Files.createTempFile("test", "out.zip").toFile();
            files.add(outFile);
            ZipUtil.zip(outFile, paths.toArray(new String[0]), streams.toArray(new InputStream[0]));
            returnCode = fileService.uploadFile(outFile);
//            FileUtils.downloadFile("out.zip",new FileInputStream(outFile),response);
        } finally {
            FileUtils.closeStream(word, docx);
            FileUtils.deleteFile(files.toArray(new File[0]));
        }
        return returnCode;
    }

    public String renameFileByExcel(InputStream excel, InputStream zip, Integer rowStartIndex,
                                    String fileNameCol, HttpServletResponse response) {
        String dir = System.getProperty("user.dir");
        File file = new File(dir + "\\extract");
        String outPath = dir + "\\rename";
        File out = new File(dir + "\\rename");
        if (!file.exists()) {
            file.mkdir();
        }
        if (!out.exists()) {
            out.mkdir();
        }
        String returnCode = null;
        List<String> names = new ArrayList<>();
        List<InputStream> streams = new ArrayList<>();
        try {

            ZipUtil.unzip(new ZipInputStream(zip), file);

            File[] files = file.listFiles();
            Arrays.sort(files, Comparator.comparing(File::getName));
            List<Map<String, String>> maps =
                    PoiUtils.readTable(excel, rowStartIndex);
            for (int i = 0; i < files.length; i++) {
                try {
                    Map<String, String> map = maps.get(i);
                    String suffix = FileUtil.getSuffix(files[i]);
                    File f = new File(outPath + "\\" + map.get(fileNameCol) + "." + suffix);
                    files[i].renameTo(f);
                    streams.add(new FileInputStream(f));
                    names.add(map.get(fileNameCol) + "." + suffix);
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage());
                }

            }
            ZipUtil.zip(out, names.toArray(new String[0]), streams.toArray(new InputStream[0]));
            returnCode = fileService.uploadFile(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeStream(zip, excel);
            FileUtils.deleteFile(file, out);
        }

        return returnCode;
    }
}
