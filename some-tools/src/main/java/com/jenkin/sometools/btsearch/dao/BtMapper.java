package com.jenkin.sometools.btsearch.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenkin.common.entity.pos.bt.BtInfoPO;
import org.apache.ibatis.annotations.Select;

/**
 * @author jenkin
 * @className FddContractKeywordsMapper
 * @description TODO
 * @date 2020/6/19 10:49
 */
@DS("dht-ds")
public interface BtMapper extends BaseMapper<BtInfoPO> {

    @Select( "SELECT " +
            "   d1.id  " +
            "  FROM " +
            "   dht d1  " +
            " where d1.creation_data = ( select MAX(d.creation_data) from dht d);")
    public Long getMaxId();

}
