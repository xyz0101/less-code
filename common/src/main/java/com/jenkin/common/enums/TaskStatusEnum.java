package com.jenkin.common.enums;

/**
 * @author jenkin
 * @className TaskStatusEnum
 * @description
 * 进行中 ：2，
 * 等待中 ：1，
 * 未开始（待审核） ：0，
 * 开始比对 3 ，
 * PDF转换 4，
 * 审核中 5，
 * 已完成且成功 ：6，
 * 已完成但失败：7，
 * 已取消：-1
 * @date 2020/9/22 15:56
 */
public enum TaskStatusEnum {




    /**
     * 已取消
     */
    CANCEL("Cancel",-1,"已取消"),
    /**
     * 未开始（待审核）
     */
    UN_START("UnStart",0,"待审核"),
    /**
     * 等待中
     */
    WAITING("Waiting",1,"等待中"),
    /**
     * 进行中
     */
    RUNNING("Running",2,"进行中"),
    /**
     * 开始比对
     */
    START_COMPARE("StartCompare",3,"开始比对"),
    /**
     * PDF转换
     */
    CONVERT_PDF("ConvertPdf",4,"PDF转换"),
    /**
     * 审核中
     */
    EXAMINING("Examining",5,"审核中"),
    /**
     * 已完成且成功
     */
    COMPLETE_SUCCESS("CompleteSuccess",6,"已完成且成功"),
    /**
     * 已完成但失败
     */
    COMPLETE_FAIL("CompleteFail",7,"已完成但失败"),
    /**
     * 已完成且成功
     */
    PASS_TASK("pass",8,"暂停");
    private String code;
    private String name;
    private Integer intCode;
    private TaskStatusEnum(String code, Integer intCode, String name){
        this.code = code;
        this.name = name;
        this.intCode = intCode;
    }


    public String getCode() {
        return code;
    }

    public Integer getIntCode() {
        return intCode;
    }


    public String getName() {
        return name;
    }

    public static TaskStatusEnum getById(Integer id){
        if (id==null) {
            return null;
        }
        TaskStatusEnum[] values = values();
        for (TaskStatusEnum value : values) {
            if (value.getIntCode()==id.intValue()) {
                return value;
            }
        }
        return null;
    }

}
