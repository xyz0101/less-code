package com.jenkin.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.Sort;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/10 21:35
 * @description：
 * @modified By：
 * @version: 1.0
 */

public class SimpleQuery<T> {
    IService<T> iService;
    MyQueryWrapper<T> queryWrapper = new MyQueryWrapper<>();



    private void setBaseQo(BaseQo baseQo) {
        this.baseQo = baseQo;
    }

    private BaseQo baseQo;
    public static <T> SimpleQuery<T> builder(BaseQo baseQo,IService<T> iService){
        SimpleQuery<T> query = new SimpleQuery<>();
        query.setBaseQo(baseQo);
        query.iService = iService;
        return query;
    }
    public  SimpleQuery<T>  sort(){
        queryWrapper.sort(baseQo.getSorts());
       return this;
    }

    public MyQueryWrapper<T> getQuery(){
        return queryWrapper;
    }

    public Page<T> getPage(){
       return new Page<>(baseQo.getPage(),baseQo.getPageSize());
    }

    public <R> Page<R> page(Class<R> rClass){
        Page<T> page = iService.page(getPage(), queryWrapper);
        List<T> records = page.getRecords();
        List<R> rs = BeanUtils.mapList(records, rClass);
        Page<R> pageR = new Page<>();
        pageR.setTotal(page.getTotal());
        pageR.setRecords(rs);
        return pageR;
    }

}
