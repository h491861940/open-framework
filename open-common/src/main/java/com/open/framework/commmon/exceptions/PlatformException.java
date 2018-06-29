package com.open.framework.commmon.exceptions;

import com.open.framework.commmon.enums.IEnumFace;

public class PlatformException extends BaseException {

    public PlatformException(Integer code, String message) {
        super(code, message);
    }

    public PlatformException(Integer code, String message, Object[] msgArgs) {
        super(code, message, msgArgs);
    }

    public PlatformException(String message) {
        super(message);
    }

    public PlatformException(String message, Object[] msgArgs) {
        super(message, msgArgs);
    }

    public PlatformException(IEnumFace iEnumFace) {
        super(iEnumFace);
    }

    public PlatformException(IEnumFace iEnumFace, Object[] msgArgs) {
        super(iEnumFace, msgArgs);
    }
}