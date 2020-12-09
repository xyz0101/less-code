package com.jenkin.simpleshiro.entity;

import lombok.Data;

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
@Data
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String roleName;

    private String roleCode;

    private String menus;

}
