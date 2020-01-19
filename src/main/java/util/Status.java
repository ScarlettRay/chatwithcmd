package util;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-12-02 13:44:03
 * <p>
 */
public enum Status {

    OK(200,"ok"),ERROR(500,"error"),NEW(201,"new");

    private Integer code;

    private String message;

    private Status(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}
