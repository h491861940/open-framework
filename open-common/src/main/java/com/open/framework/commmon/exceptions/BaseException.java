package com.open.framework.commmon.exceptions;

import com.open.framework.commmon.enums.IEnumFace;

public class BaseException extends RuntimeException {
    private Integer code;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public BaseException(IEnumFace iEnumFace) {
        super(iEnumFace.getPair().getText());
        this.code = iEnumFace.getPair().getVal();
    }

    public BaseException(Integer code, String message, Object[] msgArgs) {
        super(getFormatMsg(message,msgArgs));
        this.code = code;
    }

    public BaseException(String message, Object[] msgArgs) {
        super(getFormatMsg(message,msgArgs));
    }

    public BaseException(IEnumFace iEnumFace, Object[] msgArgs) {
        super(getFormatMsg(iEnumFace.getPair().getText(),msgArgs));
        this.code = iEnumFace.getPair().getVal();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 用%s来替换
     * @param message
     * @param msgArgs
     * @return
     */
    public static String getFormatMsg(String message,Object[] msgArgs){
        return String.format(message,msgArgs);
    }

    public static void main(String[] args) {
        System.out.println(getFormatMsg("aaaa%s%sqoweot%s",new Object[]{"1245",2,"qqwet"}));
    }
}