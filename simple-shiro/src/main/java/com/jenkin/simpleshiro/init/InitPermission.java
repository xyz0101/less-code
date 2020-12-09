package com.jenkin.simpleshiro.init;

import cn.hutool.core.util.ArrayUtil;
import com.jenkin.simpleshiro.dao.PermissionRepository;
import com.jenkin.simpleshiro.entity.Permission;
import com.jenkin.simpleshiro.shiro.ProxyUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 21:07
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Component
public class InitPermission implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private PermissionRepository permissionRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<Permission> all = permissionRepository.findAll();
        List<String> codes = getCodes(contextRefreshedEvent.getApplicationContext());
        if (CollectionUtils.isEmpty(all)) {
            all = new ArrayList<>();
            for (String code : codes) {
                all.add(new Permission(code,code));
            }
            permissionRepository.saveAll(all);
        }else{
            List<String> collect = all.stream().map(item -> item.getCode()).collect(Collectors.toList());
            codes.removeAll(collect);
            for (String code : codes) {
                all.add(new Permission(code,code));
            }
            permissionRepository.saveAll(all);
        }
    }

    private List<String> getCodes(ApplicationContext applicationContext) {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(RequestMapping.class);
        List<String> names = new ArrayList<>();
        if (!ArrayUtil.isEmpty(beanNamesForAnnotation)) {
            for (String s : beanNamesForAnnotation) {
                Object bean = applicationContext.getBean(s);
                bean = ProxyUtil.getTarget(bean);
                if (bean.getClass().isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation1 = bean.getClass().getAnnotation(RequestMapping.class);
                    String[] value1 = annotation1.value();
                    String s2 = value1[0];
                    if (s2.startsWith("/")) {
                        s2 = s2.substring(1);
                    }
                    s2=s2.replaceAll("/",":");
                    for (Method method : bean.getClass().getDeclaredMethods()) {
                        boolean flag=false;
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping annotation = method.getAnnotation(GetMapping.class);
                            String[] value = annotation.value();
                            String s1 = value[0];
                            s1=s1.replaceAll("/",":");
                            names.add(s2+s1);
                            flag=true;
                        }
                        if (method.isAnnotationPresent(PostMapping.class)) {
                            PostMapping annotation = method.getAnnotation(PostMapping.class);
                            String[] value = annotation.value();
                            String s1 = value[0];
                            s1=s1.replaceAll("/",":");
                            names.add(s2+s1);
                            flag=true;
                        }
                        if (method.isAnnotationPresent(DeleteMapping.class)) {
                            DeleteMapping annotation = method.getAnnotation(DeleteMapping.class);
                            String[] value = annotation.value();
                            String s1 = value[0];
                            s1=s1.replaceAll("/",":");
                            names.add(s2+s1);
                            flag=true;
                        }
                        if (method.isAnnotationPresent(PutMapping.class)) {
                            PutMapping annotation = method.getAnnotation(PutMapping.class);
                            String[] value = annotation.value();
                            String s1 = value[0];
                            s1=s1.replaceAll("/",":");
                            names.add(s2+s1);
                            flag=true;
                        }
                        if (!flag&&method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                            String[] value = annotation.value();
                            if (value.length==0) {
                                continue;
                            }
                            String s1 = value[0];
                            s1=s1.replaceAll("/",":");
                            names.add(s2+s1);
                        }

                    }
                }
            }
        }

        return names;
    }
}
