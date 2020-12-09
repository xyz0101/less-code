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
@Table(name = "menu")
@Entity
@FieldNameConstants
@Data
public class MenuPo extends BasePo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    private Integer parent;

    private String permissions;

}
