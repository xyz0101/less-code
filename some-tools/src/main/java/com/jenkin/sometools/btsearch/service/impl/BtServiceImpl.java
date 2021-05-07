package com.jenkin.sometools.btsearch.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.dozermapper.core.Mapper;

import com.jenkin.common.entity.pos.bt.BtInfoPO;

import com.jenkin.sometools.btsearch.dao.BtMapper;
import com.jenkin.sometools.btsearch.service.BtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author jenkin
 * @className LabelServiceImpl
 * @description TODO
 * @date 2020/6/28 15:17
 */
@Service
@DS("dht-ds")
public class BtServiceImpl extends ServiceImpl<BtMapper, BtInfoPO> implements BtService {
    @Autowired
    private Mapper mapper;



}
