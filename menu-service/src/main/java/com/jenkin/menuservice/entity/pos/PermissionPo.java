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
@Table(name = "permission")
@Entity
@FieldNameConstants
@Data
public class PermissionPo extends BasePo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    public PermissionPo() {
    }

    public PermissionPo(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
