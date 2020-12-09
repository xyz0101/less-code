package com.jenkin.menuservice.entity.pos;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:31
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Table(name = "role")
@Entity
@FieldNameConstants
@Data
public class RolePo extends BasePo implements Serializable {


    private String roleName;

    private String roleCode;

    private String menus;

}
