package com.gdut.graduation.handler;

import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description 捕获异常
 * @Author Skye
 * @Date 2019/3/24 20:41
 * @Version 1.0
 **/
@ControllerAdvice
public class GraduationExceptionHandler {


    @ExceptionHandler(GraduationException.class)
    @ResponseBody
    public ResultVo handlerException(GraduationException e){
        return ResultVo.createByError(e.getCode(),e.getMsg());
    }
}
