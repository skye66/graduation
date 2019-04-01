package com.gdut.graduation.exception;

import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.vo.ResultVo;
import lombok.Getter;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/24 20:15
 * @Version 1.0
 **/
@Getter
public class GraduationException extends RuntimeException {
    private int code;
    private String msg;
    public GraduationException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.msg = resultEnum.getMsg();
        this.code = resultEnum.getCode();
    }
    public GraduationException(int code,String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
