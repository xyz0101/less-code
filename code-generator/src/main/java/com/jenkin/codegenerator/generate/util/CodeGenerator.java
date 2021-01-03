package com.jenkin.codegenerator.generate.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.jenkin.codegenerator.entity.CodeGenerateInfo;
import com.jenkin.common.entity.dtos.generate.ColumnInfoDto;
import com.jenkin.common.utils.FileUtils;
import com.jenkin.common.utils.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/13 13:37
 * @description：代码生成器
 * @modified By：
 * @version: 1.0
 */
@Slf4j
public class CodeGenerator {




    public static void main(String[] args) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        Template template = velocityEngine.getTemplate("/template/controller/Controller.java.vm","UTF-8");

        VelocityContext ctx = new VelocityContext();
        ctx.put("author","jenkin");
        ctx.put("createDate",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        ctx.put("comments","测试生成");
        ctx.put("moduleName","test");
        ctx.put("className","TestBean");
        ctx.put("classNameLower","testBean");
        ctx.put("packageName","com.jenkin.codegenerate");
        ctx.put("entityPackageName","com.jenkin.common");
        CodeGenerateInfo codeGenerateInfo = new CodeGenerateInfo();
        ctx.put("info",codeGenerateInfo);
//        StringWriter sw = new StringWriter();
        try {

            FileWriter fileWriter = new FileWriter("D:\\tempDir\\TestBeanController.java");
            template.merge(ctx,fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(sw.toString());
        File file = new File("D:\\tempDir\\a.zip");
        File src = new File("D:\\tempDir\\test");
        ZipUtil.zip(file,false,src);

    }


    public static void generateCode(CodeGenerateInfo info,OutputStream outputStream){

        String[] ignores = {"id", "delete_flag", "created_by", "creation_date", "last_update_date", "last_updated_by", "version_number"};
        VelocityEngine velocityEngine =initVelocityEngine();
        VelocityContext ctx = new VelocityContext();
        List<ColumnInfoDto> collect = info.getTableInfo().getColumns().stream().filter(item ->
                !Arrays.asList(ignores).contains(item.getName().toLowerCase())).collect(Collectors.toList());
        info.getTableInfo().setColumns(collect);
        ctx.put("info",info);
        ctx.put("/template/controller/Controller.java.vm",info.getClassName()+"Controller.java");
        ctx.put("/template/service/Service.java.vm",info.getClassName()+"Service.java");
        ctx.put("/template/service/impl/ServiceImpl.java.vm",info.getClassName()+"ServiceImpl.java");
        ctx.put("/template/dao/Mapper.java.vm",info.getClassName()+"Mapper.java");
        ctx.put("/template/entity/vos/system/Vo.java.vm",info.getClassName()+"Vo.java");
        ctx.put("/template/entity/pos/system/Po.java.vm",info.getClassName()+"Po.java");
        ctx.put("/template/entity/qos/system/Qo.java.vm",info.getClassName()+"Qo.java");
        ctx.put("/template/entity/dtos/system/Dto.java.vm",info.getClassName()+"Dto.java");
        ctx.put("/template/api/TemplateResponseBean.ts.vm",info.getClassName()+"Bean.ts");
        ctx.put("/template/api/TemplateApiPath.ts.vm",info.getClassName()+"ApiPath.ts");
        ctx.put("/template/fservice/template.service.spec.ts.vm",info.getClassName()+".service.spec.ts");
        ctx.put("/template/fservice/template.service.ts.vm",info.getClassName()+".service.ts");
        try {
            generateFile(velocityEngine,ctx,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void generateFile(VelocityEngine velocityEngine, VelocityContext ctx, OutputStream outputStream) throws IOException {
        String rootPath = System.getProperty("user.dir")+("/0-generate/"+ ShiroUtils.getUserCode()+"/"+UUID.randomUUID()+"/");
        File root =  createFile(rootPath);
        try {
            Map<String, List<Template>> templates = getTemplate(velocityEngine);
            for (String k : templates.keySet()) {
                List<Template> v = templates.get(k);
                if (!CollectionUtils.isEmpty(v)) {
                    //当前目录，例如 controller
                    String dicPath = rootPath + "/" + k+"/";
                    File dic =  createFile(dicPath);
                    for (Template template : v) {
                        //目标文件名称
                        String name = (String) ctx.get(template.getName());
                        String filePath = dicPath + "/" + name;
                        //当前文件
                        File file = createFile(filePath);
                        FileWriter fileWriter =null;
                        try {
                            fileWriter = new FileWriter(file);
                            template.merge(ctx,  fileWriter);
                            fileWriter.flush();
                        }finally {
                            FileUtils.closeStream(fileWriter);
                        }
                    }
                }else{
                    log.error("{} 为空",k);
                }
            }
            ZipUtil.zip(outputStream, CharsetUtil.defaultCharset(),false,null,root);
        }finally {
            FileUtils.deleteFile(root);
        }


    }

    private static File createFile(String filePath) throws IOException {
        //当前文件
        File file = new File(filePath);

        if (!file.exists()) {
            if (filePath.endsWith("/")) {
               file.mkdirs();
            }else{
                file.createNewFile();
            }
        }
        return file;
    }

    private static Map<String, List<Template>> getTemplate(VelocityEngine velocityEngine) {

        Map<String,List<Template>> templates = new HashMap<>();
        List<Template> controller = Collections.singletonList(velocityEngine.getTemplate("/template/controller/Controller.java.vm","UTF-8"));
        List<Template> mapper = Collections.singletonList(velocityEngine.getTemplate("/template/dao/Mapper.java.vm","UTF-8"));
        List<Template> entity = new ArrayList<>();
        List<Template> apis = new ArrayList<>();
        List<Template> fservices = new ArrayList<>();
        Template dto = velocityEngine.getTemplate("/template/entity/dtos/system/Dto.java.vm","UTF-8");
        Template po = velocityEngine.getTemplate("/template/entity/pos/system/Po.java.vm","UTF-8");
        Template vo = velocityEngine.getTemplate("/template/entity/vos/system/Vo.java.vm","UTF-8");
        Template qo = velocityEngine.getTemplate("/template/entity/qos/system/Qo.java.vm","UTF-8");
        entity.add(dto);
        entity.add(po);
        entity.add(vo);
        entity.add(qo);
        List<Template> service = new ArrayList<>();
        Template serviceTemplate = velocityEngine.getTemplate("/template/service/Service.java.vm","UTF-8");
        Template serviceImplTemplate = velocityEngine.getTemplate("/template/service/impl/ServiceImpl.java.vm","UTF-8");
        service.add(serviceTemplate);
        service.add(serviceImplTemplate);
        Template templateResponseBean = velocityEngine.getTemplate("/template/api/TemplateResponseBean.ts.vm","UTF-8");
        Template templateApiPath = velocityEngine.getTemplate("/template/api/TemplateApiPath.ts.vm","UTF-8");
        apis.add(templateResponseBean);
        apis.add(templateApiPath);
        Template serviceSpecTemplate = velocityEngine.getTemplate("/template/fservice/template.service.spec.ts.vm","UTF-8");
        Template fserviceTemplate = velocityEngine.getTemplate("/template/fservice/template.service.ts.vm","UTF-8");
        fservices.add(serviceSpecTemplate);
        fservices.add(fserviceTemplate);




        templates.put("controller",controller);
        templates.put("mapper",mapper);
        templates.put("entity",entity);
        templates.put("service",service);
        templates.put("api",apis);
        templates.put("fservice",fservices);


        return templates;
    }

    private static VelocityEngine initVelocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
        return velocityEngine;

    }

}
