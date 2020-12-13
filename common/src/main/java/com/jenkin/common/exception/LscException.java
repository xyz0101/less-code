package com.jenkin.common.exception;

import org.springframework.util.StringUtils;

import static com.jenkin.common.exception.ExceptionEnum.*;

/**
 * @author ：jenkin
 * @date ：Created at 2020/12/12 14:49
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class LscException extends RuntimeException {
    private static final LscException systemException = new LscException(ERROR_EXCEPTION,ERROR_EXCEPTION.getDesc());
    private ExceptionEnum type;
    private String msg;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public LscException(ExceptionEnum type, String msg) {
        super(StringUtils.isEmpty(msg)?type.getDesc():msg);
        this.type = type;
        this.msg = StringUtils.isEmpty(msg)?type.getDesc():msg;
    }
    public LscException(ExceptionEnum type ) {
        super( type.getDesc());
        this.type = type;
        this.msg =  type.getDesc() ;
    }

    public static LscException systemException(String msg ){
        return  new LscException(ERROR_EXCEPTION,msg==null?ERROR_EXCEPTION.getDesc():msg);
    }

    public ExceptionEnum getType() {
        return type;
    }

    private void setType(ExceptionEnum type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        this.msg = msg;
    }
}
