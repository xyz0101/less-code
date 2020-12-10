package com.jenkin.common.entity.pos.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.jenkin.common.entity.pos.BasePo;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:31
 * @description：
 * @modified By：
 * @version: 1.0
 */
@TableName("lsc_menu")
@FieldNameConstants
@Table(name = "lsc_menu",comment = "菜单表")
@Data
public class MenuPo extends BasePo implements Serializable {

    @Column(comment = "菜单名称")
    private String name;
    @Column(comment = "菜单编号")
    private String code;
    @Column(comment = "菜单父ID")
    private Integer parent;
    @Column(comment = "菜单权限ID集合")
    private String permissions;
    private String menuUrl;
    @Column(comment = "菜单的图标")
    private String menuIcon;
    @Column(comment = "菜单的顺序")
    private Integer menuOrder;
    @Column(comment = "菜单类型，1：菜单，2、按钮")
    private Integer menuType;

}
