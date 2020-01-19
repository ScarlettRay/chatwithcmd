package util;

import lombok.Data;

/**
 * @author Administrator
 * @create 2019-12-23 15:20:36
 * <p>
 */
@Data
public class Result<T> {

    public static final Result OK = new Result(Status.OK);

    public static final Result ERROR = new Result(Status.ERROR);


    private int statusCode;
    private String message;
    private T t;

    public Result(int code,String message){
        this.statusCode = code;
        this.message = message;
    }

    public Result(Status status){
        this.statusCode = status.getCode();
        this.message = status.getMessage();
    }

    public Result(Exception e){
        this.statusCode = 500;
        this.message = e.getMessage();
    }

    public boolean isOK(){
        return statusCode == 200;
    }
}
