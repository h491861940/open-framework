package com.open.framework.demo.repository;

import com.open.framework.demo.model.Demo;
import com.open.framework.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DemoRepository extends JpaRepository<Demo,String> {

}