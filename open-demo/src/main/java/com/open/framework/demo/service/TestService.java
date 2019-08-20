package com.open.framework.demo.service;

import com.open.framework.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TestService {
    void save();
    void modify();

}