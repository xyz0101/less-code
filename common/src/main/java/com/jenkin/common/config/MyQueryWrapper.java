package com.jenkin.common.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jenkin.common.entity.qos.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：jenkin
 * @date ：Created at 2020/3/13 12:07
 * @description：条件构造器，重写字符串转换方法
 * @modified By：
 * @version: 1.0
 */
public class
MyQueryWrapper<T> extends QueryWrapper<T> {
    /**
     * 关联查询构造器
     */
    private final List<JoinBuilder> joinBuilder = new ArrayList<>();


    /**
     * 获取 columnName
     *
     * @param column
     */
    @Override
    protected String columnToString(String column) {
        return StringUtils.camelToUnderline(column);
    }


    public static<T> MyQueryWrapper<T> query(){
        return new MyQueryWrapper<T>();
    }


    /**
     * 关联查询构造
     * 使用示例
     *   MyQueryWrapper<TemplatePO> queryWrapper = new  MyQueryWrapper<>();
     *         queryWrapper.addJoin(
     *                 JoinBuilder.build().selectField("label_suit_code","label_suit_name","label_suit_type")
     *                         .join(JoinBuilder.LEFT,"template","label_suit")
     *                         .on(CaseBuilder.build().eq("suit_id","id").and().eq(0,"delete_flag"))
     *         ).addJoin(
     *                 JoinBuilder.build().selectField("real_name")
     *                         .join(JoinBuilder.LEFT,"template","user")
     *                         .on(CaseBuilder.build().eq("created_by","user_code").and().eq(0,"delete_flag" ))
     *         ).like(TemplatePO.Fields.labelSuitName,"风险")
     *         ;
     *         List<TemplatePO> list = templateService.list(queryWrapper);
     *
     * -------------------------------生成的SQL------------------------------------------------
     * SELECT
     *     mainjointable.id,
     *     mainjointable.template_code,
     *     mainjointable.template_name,
     *     mainjointable.template_url,
     *     mainjointable.suit_id,
     *     mainjointable.extract_type,
     *     mainjointable.template_tag_type,
     *     mainjointable.child_source_id,
     *     mainjointable.source_id,
     *     mainjointable.parent_id,
     *     mainjointable.repetition,
     *     mainjointable.template_note,
     *     mainjointable.is_complete,
     *     mainjointable.delete_flag,
     *     mainjointable.created_by,
     *     mainjointable.creation_date,
     *     mainjointable.last_update_date,
     *     mainjointable.last_updated_by,
     *     mainjointable.version_number,
     *     subjointable0.LABEL_SUIT_NAME,
     *     subjointable0.LABEL_SUIT_TYPE,
     *     subjointable0.LABEL_SUIT_CODE,
     *     subjointable1.REAL_NAME
     * FROM
     *     template AS mainjointable
     * LEFT JOIN label_suit AS subjointable0 ON (
     *     mainjointable.suit_id = subjointable0.id
     *     AND 0 = subjointable0.delete_flag
     * )
     * LEFT JOIN user  AS subjointable1 ON (
     *     mainjointable.created_by = subjointable1.user_code
     *     AND 0 = subjointable1.delete_flag
     * )
     * WHERE
     *     mainjointable.delete_flag = 0
     * AND (
     *     subjointable0.label_suit_name LIKE ?
     * )
     *
     * @param builder
     * @return
     */
    public  MyQueryWrapper<T> addJoin(JoinBuilder builder){
        this.joinBuilder.add(builder);
        return this;
    }

    public List<JoinBuilder> getJoinBuilder() {
        return joinBuilder;
    }
    /**
     * 排序
     * @param sorts
     * @return
     */
    public QueryWrapper<T> sort(List<Sort> sorts){
        if(!CollectionUtils.isEmpty(sorts)){
            sorts.forEach(item->{
                orderBy(item.getSortField()!=null,"asc".equals(item.getSortValue()),item.getSortField());
            });
        }
        return this;
    }

}
