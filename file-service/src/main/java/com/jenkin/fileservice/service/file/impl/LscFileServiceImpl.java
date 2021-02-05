package com.jenkin.fileservice.service.file.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenkin.common.config.MyQueryWrapper;
import com.jenkin.common.entity.dtos.file.LscFileDto;
import com.jenkin.common.entity.pos.file.LscFilePo;
import com.jenkin.common.entity.qos.BaseQo;
import com.jenkin.common.entity.qos.file.LscFileQo;
import com.jenkin.common.exception.ExceptionEnum;
import com.jenkin.common.exception.LscException;
import com.jenkin.common.files.fileservice.FileService;
import com.jenkin.common.utils.BeanUtils;
import com.jenkin.common.utils.SimpleQuery;
import com.jenkin.fileservice.dao.file.LscFileMapper;
import com.jenkin.fileservice.service.file.LscFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author ：jenkin
 * @date ：Created at 2021-01-03 20:14:19
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Service
public class LscFileServiceImpl extends ServiceImpl<LscFileMapper, LscFilePo> implements LscFileService {
    @Resource
    private FileService fileService;
    @Override
    public LscFileDto getById(Integer id) {
        return BeanUtils.map(super.getById(id),LscFileDto.class);
    }

    /**
     * 分页获取数据
     *
     * @param  lscFile
     */
    @Override
    public Page<LscFileDto> listByPage(BaseQo<LscFileQo>  lscFile) {
        SimpleQuery<LscFilePo> simpleQuery = SimpleQuery.builder( lscFile,this).sort();
        MyQueryWrapper<LscFilePo> query = simpleQuery.getQuery();
        LscFileQo data = lscFile.getData();
        query.like(!StringUtils.isEmpty(data.getFileName()),LscFilePo.Fields.fileName, data.getFileName());
        query.like(!StringUtils.isEmpty(data.getFileType()), LscFilePo.Fields.fileType, data.getFileType());
        query.eq(LscFilePo.Fields.newFlag,true);
        Page<LscFileDto> page = simpleQuery.page(LscFileDto.class);
        Collection<String> collect = page.getRecords().stream().map(LscFilePo::getSourceFileCode).collect(Collectors.toList());
        Map<String, List<LscFileDto>> historyMap = this.getHistoryMap(collect);
        page.getRecords().forEach(item->item.setHistory(historyMap.get(item.getSourceFileCode())));

        return page;
    }

    private Map<String, List<LscFileDto>> getHistoryMap(Collection<String> collect) {
        Map<String, List<LscFileDto>> res = new HashMap<>();
        if (CollectionUtils.isEmpty(collect)) {
            return res;
        }
        MyQueryWrapper<LscFilePo> query = new MyQueryWrapper<>();
        query.in(LscFilePo.Fields.sourceFileCode,collect);
        query.eq(LscFilePo.Fields.newFlag,false);
        List<LscFilePo> list = list(query);
        list.forEach(item->{
            List<LscFileDto> history = res.get(item.getSourceFileCode());
            if (history==null) {
                history = new ArrayList<>();
            }
            history.add(BeanUtils.map(item,LscFileDto.class));
            res.put(item.getSourceFileCode(),history);
        });
        return res;
    }

    /**
     * 保存信息
     *
     * @param  lscFile
     * @return
     */
    @Override
    public LscFileDto saveLscFileInfo(LscFileDto  lscFile) {
        saveOrUpdate( lscFile);
        return  lscFile;
    }

    /**
     * 更新文件
     *
     * @param stream
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFile(InputStream stream, int id) {
        LscFileDto byId = getById(id);
        String code = fileService.uploadFile(stream, "test.docx");
        if (byId==null) {
            throw new LscException(ExceptionEnum.ERROR_PARAM_EXCEPTION);
        }
        if(byId.getNewFlag()) {
            String sourceFileCode = byId.getSourceFileCode() == null ? byId.getFileCode() : byId.getSourceFileCode();
            byId.setSourceFileCode(sourceFileCode);
            byId.setNewFlag(false);
            byId.setLastUpdateDate(LocalDateTime.now());
            Integer version = byId.getFileVersion();
            saveOrUpdate(byId);
            byId.setCreationDate(null);
            byId.setLastUpdateDate(null);
            byId.setFileVersion(version + 1);
            byId.setId(null);
            byId.setNewFlag(true);
            byId.setFileCode(code);
            save(byId);
        }else{
            byId.setFileCode(code);
            byId.setNewFlag(false);
            byId.setLastUpdateDate(LocalDateTime.now());
            saveOrUpdate(byId);
        }

    }
}
