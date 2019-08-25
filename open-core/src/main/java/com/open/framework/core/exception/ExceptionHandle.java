package com.open.framework.core.exception;

import com.open.framework.commmon.enums.EnumBase;
import com.open.framework.commmon.exceptions.BaseException;
import com.open.framework.commmon.exceptions.BusinessException;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.web.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

@ControllerAdvice
public class ExceptionHandle {
    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult Handle(Exception e){

        if (e instanceof Exception){

            return JsonResult.error(EnumBase.PlatformCode.SYSTEM_ERROR.getVal(),EnumBase.PlatformCode.SYSTEM_ERROR.getText()+getStackTrace(e.fillInStackTrace()));
        }else {
            //将系统异常以打印出来
            logger.info("[系统异常]{}",e);
            return JsonResult.error(-1,"未知错误");
        }
    }
    @ExceptionHandler({BaseException.class,PlatformException.class,BusinessException.class})
    @ResponseBody
    public JsonResult handleBaseException(BaseException e) {
        //可以合并也可以分开处理,目前合并处理
        if (e instanceof PlatformException){
            PlatformException platformException = (PlatformException) e;
            return JsonResult.error(platformException.getCode(),"平台异常:  "+platformException.getMessage());
        }else if (e instanceof BusinessException){
            BusinessException businessException = (BusinessException) e;
            return JsonResult.error(businessException.getCode(),"业务异常:  "+businessException.getMessage());
        }else {
            //将系统异常以打印出来
            return JsonResult.error(-1,"未知错误");
        }
    }
    //@ExceptionHandler(PlatformException.class)
    //@ResponseBody
    public JsonResult handleBusinessException(PlatformException e) {
        if (e instanceof PlatformException){
            PlatformException platformException = (PlatformException) e;
            return JsonResult.error(platformException.getCode(),"平台异常:  "+platformException.getMessage());
        }else {
            //将系统异常以打印出来
            logger.info("[平台异常]{}",e);
            return JsonResult.error(-1,"未知错误");
        }
    }
    //@ExceptionHandler(BusinessException.class)
    //@ResponseBody
    public JsonResult handleBusinessException(BusinessException e) {
        if (e instanceof BusinessException){
            BusinessException businessException = (BusinessException) e;
            return JsonResult.error(businessException.getCode(),"业务异常:  "+businessException.getMessage());
        }else {
            //将系统异常以打印出来
            logger.info("[业务异常]{}",e);
            return JsonResult.error(-1,"未知错误");
        }
    }

    public static String getStackTrace(Throwable aThrowable) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

}