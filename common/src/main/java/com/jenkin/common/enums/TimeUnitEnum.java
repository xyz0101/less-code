package com.jenkin.common.enums;

public enum TimeUnitEnum {

    /**
     * 分钟
     */
    MINUTE("minute",1,"分钟"),
    /**
     *小时
     */
    UN_START("hour",2,"小时"),
    /**
     * 天
     */
    WAITING("day",3,"天")
    ;

    private String code;
    private String name;
    private Integer intCode;
    private TimeUnitEnum(String code, Integer intCode, String name){
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

    public static TimeUnitEnum getById(Integer id){
        if (id==null) {
            return null;
        }
        TimeUnitEnum[] values = values();
        for (TimeUnitEnum value : values) {
            if (value.getIntCode()==id.intValue()) {
                return value;
            }
        }
        return null;
    }

}
