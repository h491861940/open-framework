package com.open.framework.demo.service.impl;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.demo.model.User;
import com.open.framework.demo.repository.UserRepository;
import com.open.framework.demo.service.Test2Service;
import com.open.framework.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
@Service
public class TestServiceImpl implements TestService {
    @Autowired(required = false)
    private UserRepository userRepository;
    @Autowired(required = false)
    private Test2Service test2Service;

    @Override
    public void save() {
        User u = new User();
        u.setName("hsj");
        u.setPassword("123456");
        u.setLoginName("hsj");
        u.setLoginDate(new Date());
        userRepository.save(u);// 保存数据.第一个save
        //throw new PlatformException("aaa");
        test2Service.save();//第二个save
    }

    @Override
    public void modify() {
        User u = userRepository.findById("111").orElse(null);
        System.out.println("1111的用户"+u.toString());
        u.setName("1111");
        u.setLoginDate(new Date());
        new Thread(() -> {
            User u1 = userRepository.findById("111").orElse(null);
            System.out.println("33333的用户"+ u1.toString());
            u1.setName("333");
            u1.setLoginDate(new Date());

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            userRepository.save(u1);
        }).start();
        userRepository.save(u);
    }

}
