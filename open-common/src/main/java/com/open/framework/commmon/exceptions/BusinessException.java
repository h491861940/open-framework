package com.open.framework.commmon.exceptions;

import com.open.framework.commmon.enums.IEnumFace;

public class BusinessException extends BaseException {

    public BusinessException(Integer code, String message) {
        super(code, message);
    }

    public BusinessException(Integer code, String message, Object[] msgArgs) {
        super(code, message, msgArgs);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object[] msgArgs) {
        super(message, msgArgs);
    }

    public BusinessException(IEnumFace iEnumFace) {
        super(iEnumFace);
    }

    public BusinessException(IEnumFace iEnumFace, Object[] msgArgs) {
        super(iEnumFace, msgArgs);
    }
}