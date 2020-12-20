package com.jenkin.codegenerator.generate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.codegenerator.generate.dao.TableInfoMapper;
import com.jenkin.codegenerator.generate.service.TableInfoService;
import com.jenkin.common.entity.pos.generate.TableInfoPo;
import org.springframework.stereotype.Service;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/19 17:58
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfoPo> implements TableInfoService {
}
