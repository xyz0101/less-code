package com.jenkin.common.exception;

public enum ExceptionEnum {

    /**
     *
     */
    ERROR_PARAM_EXCEPTION("400","参数错误"),
    NO_AUTH_EXCEPTION("401","账户未授权"),
    FORBIDDEN_ACCESS_EXCEPTION("403","禁止访问"),
    ERROR_EXCEPTION("500","系统错误"),

    SQL_ERROR_EXCEPTION("510","不合法的SQL语句，请检查配置项是否完成！"),
    DELETE_TOOMUCH_EXCEPTION("511","删除参数不能为空"),
    QRCODE_LOGIN_ERROR_EXCEPTION("512","授权过期，需要重新扫描二维码"),
    ERROR_QUESTION_EXCEPTION("513","问题有误，请重新开始任务"),
    ERROR_START_EXCEPTION("514","正在答题ing，请勿重复点击")
            ;
    ExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

//    public void setCode(String code) {
//        this.code = code;
//    }

    public String getDesc() {
        return desc;
    }

//    public void setDesc(String desc) {
//        this.desc = desc;
//    }


}
