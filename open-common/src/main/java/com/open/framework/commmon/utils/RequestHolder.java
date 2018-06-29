package com.open.framework.commmon.utils;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHolder {

    public static HttpServletRequest getRequest() {
        HttpServletRequest req;
        try {
            req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return req;
        } catch (Exception e) {
            //e.printStackTrace();
        } 
        return null;
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse resp;
        try {
            resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            return resp;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

}
