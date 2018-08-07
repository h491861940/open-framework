package com.open.framework.demo.repository;

import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.demo.model.Demo;
import com.open.framework.demo.model.Student;
import com.open.framework.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface DemoRepository extends BaseRepository<Demo,String>{

}