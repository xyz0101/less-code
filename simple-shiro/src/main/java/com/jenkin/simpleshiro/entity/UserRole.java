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
@Data
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {
    @Id // @Id: 指明id列, 必须
    @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    private Integer id;
    private Integer userId;
    private Integer roleId;


}
