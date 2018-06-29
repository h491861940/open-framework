package com.open.framework.demo.repository;

import com.open.framework.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "select password,loginName from User b where b.name like %:name%")
    List<User> getInfo(@Param("name") String name);

    List<User> findByName(String name);
}