package com.jenkin.menuservice.entity.pos;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/3 20:30
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Data
@FieldNameConstants
@Entity
@Table(name = "user")
public class UserPo extends BasePo implements Serializable {

    @Column(name = "name", nullable = false)
    private String userName;
    private String userCode;
    private String password;

    private String salt;

}
