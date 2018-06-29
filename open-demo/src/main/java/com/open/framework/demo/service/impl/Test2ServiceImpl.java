package com.open.framework.demo.service.impl;

import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.dao.dynamic.ChangeDs;
import com.open.framework.demo.model.Demo;
import com.open.framework.demo.repository.DemoRepository;
import com.open.framework.demo.service.Test2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Test2ServiceImpl implements Test2Service {
    @Autowired(required = false)
    private DemoRepository demoRepository;

    @Override
    @ChangeDs(key = "ds1")
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void save() {
        //需要回滚的事物,需要手动申明
        Demo demo = new Demo();
        demo.setCode("aa");
        demo.setName("bbb");
        demoRepository.save(demo);
        throw new PlatformException("aaa");
    }
}
