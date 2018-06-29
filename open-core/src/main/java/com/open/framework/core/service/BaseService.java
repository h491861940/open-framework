package com.open.framework.core.service;

import org.springframework.data.domain.Example;

import java.io.Serializable;

public interface BaseService<T, S> {

    T save(T t);

    T saveDto(S dto);

    S getDto(String id);

    T get(String id);

    void delete(String id);

    void delete(T t);

    boolean existsById(String id);

    public boolean exists(Example<T> example);
}