package com.jenkin.common.entity.vos.tasks;

import lombok.Data;

/**
 * @author jenkin
 * @className TriggerVO
 * @description WebSocket触发实体
 * @date 2020/9/23 14:43
 */
@Data
public class TriggerVO {

    private String triggerCode;

    private String[] triggerUsers;

    private String[] triggerRoles;

    private String triggerMsg;



}
