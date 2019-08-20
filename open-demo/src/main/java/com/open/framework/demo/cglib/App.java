package com.open.framework.demo.cglib;

import com.open.framework.demo.proxy.UserDao;

public class App {

    public static void main(String[] args) {
        //目标对象
        UserDao target = new UserDao();

        //代理对象
        UserDao proxy = (UserDao)new ProxyFactory().getProxyInstance(target);

        //执行代理对象的方法
        proxy.save();
    }
}