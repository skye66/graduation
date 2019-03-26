package com.gdut.graduation.vo;


import com.gdut.graduation.enums.ResultEnum;
import lombok.Getter;

/**
 * @Description 返回给前端的结果
 * @Author Skye
 * @Date 2019/3/24 20:18
 * @Version 1.0
 **/
@Getter
public class ResultVo<T> {
    private int code;
    private String msg;
    private T data;
    private ResultVo(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    private ResultVo(T data,int code,String msg){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回一个成功的消息
     * @param code 响应码
     * @param msg 响应消息
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static<T> ResultVo<T> createBySuccess(int code,String msg){
        return new ResultVo<>(code,msg);
    }

    public static<T> ResultVo<T> createBySuccess(T data,int code,String msg){
        return new ResultVo<>(data,code,msg);
    }
    public static<T> ResultVo<T> createBySuccess(T data){
        return new ResultVo<>(data,ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
    }

    public static<T> ResultVo<T> createBySuccess(int code){
        return new ResultVo<>(code, ResultEnum.SUCCESS.getMsg());
    }
    public static<T> ResultVo<T> createBySuccess(){
        return new ResultVo<>(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
    }
    public static <T> ResultVo<T> createByError(){
        return new ResultVo<>(ResultEnum.ERROR.getCode(),ResultEnum.ERROR.getMsg());
    }
    public static <T> ResultVo<T> createByError(int code){
        return new ResultVo<>(code,ResultEnum.ERROR.getMsg());
    }
    public static <T> ResultVo<T> createByError(int code,String msg){
        return new ResultVo<>(code,msg);
    }

    public static <T> ResultVo<T> createByError(ResultEnum resultEnum){
        return new ResultVo<>(resultEnum.getCode(),resultEnum.getMsg());
    }


}
