package com.jenkin.menuservice;

import com.github.dozermapper.core.Mapper;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jenkin
 * @className BeanUtils
 * @description 
 * @date 2020/6/19 9:58
 */
public class BeanUtils {

    public static Mapper mappers  = ApplicationContextProvider.getBean(Mapper.class);

    /**
     * 将集合转成集合
     * List<A> -->  List<B>
     *
     * @param sourceList       源集合
     * @param destinationClass 待转类型
     * @param <T>
     * @return
     */
    public static <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
            return Collections.emptyList();
        }
        return sourceList.parallelStream()
                .filter(Objects::nonNull)
                .map((sourceObject) -> mappers.map(sourceObject, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * 对象转对象
     *
     * @param var1
     * @param var2
     * @param <T>
     * @return
     */
    public static <T> T map(Object var1, Class<T> var2,String... ignores) {
        T map = mappers.map(var1, var2);
        removeFields(map,var2,ignores);
        return map;
    }

    private static  void removeFields(Object map,  Class<?> var2,String[] ignores) {
        for (String ignore : ignores) {
            try {
                Field declaredField = var2.getDeclaredField(ignore);
                if (declaredField!=null) {
                    declaredField.setAccessible(true);
                    declaredField.set(map,null);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();

            }
        }
    }

    public static void map(Object var1, Object var2,String... ignores) {
        mappers.map(var1, var2);
        removeFields(var2,var2.getClass(),ignores);
    }

    
    public static <S, T> List<T> mapList(final Mapper mapper, List<S> sourceList, Class<T> targetObjectClass) {
        List<T> resList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (S item : sourceList) {
                if (item!=null) {
                    T res = mapper.map(item, targetObjectClass);
                    resList.add(res);
                }else{
                    resList.add(null);
                }
            }
        }
        return resList;
    }
}
