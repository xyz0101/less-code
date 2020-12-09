package com.jenkin.menuservice.init;

import cn.hutool.core.util.ArrayUtil;
import com.jenkin.menuservice.anno.MyPermission;
import com.jenkin.menuservice.entity.pos.PermissionPo;
import com.jenkin.menuservice.service.PermissionService;
import com.jenkin.menuservice.shiro.ProxyUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    private PermissionService permissionService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<PermissionPo> all = permissionService.list();
        List<String> codes = getCodes(contextRefreshedEvent.getApplicationContext());
        if (CollectionUtils.isEmpty(all)) {
            all = new ArrayList<>();
            for (String code : codes) {
                all.add(new PermissionPo(code,code));
            }
            permissionService.saveBatch(all);
        }else{
            List<String> collect = all.stream().map(item -> item.getCode()).collect(Collectors.toList());
            codes.removeAll(collect);
            for (String code : codes) {
                all.add(new PermissionPo(code,code));
            }
            permissionService.saveBatch(all);
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
                    for (Method method : bean.getClass().getDeclaredMethods()) {
                        if (method.isAnnotationPresent(MyPermission.class)) {
                            MyPermission annotation = method.getAnnotation(MyPermission.class);
                            String[] value = annotation.value();
                            names.addAll(Arrays.asList(value));
                        }

                    }
                }
            }
        }

        return names;
    }
}
