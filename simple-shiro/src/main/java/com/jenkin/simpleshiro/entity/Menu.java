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
@Table(name = "menu")
@Entity
@Data
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String code;

    private Integer parent;

    private String permissions;

}
