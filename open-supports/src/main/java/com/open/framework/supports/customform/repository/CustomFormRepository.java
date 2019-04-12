package com.open.framework.supports.customform.repository;


import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.supports.customform.entity.CustomForm;

import java.util.List;


/**
 * @Author hsj
 * @Description 表单的数据库维护
 * @Date  2019-03-25 16:11:49
 **/
public interface CustomFormRepository extends BaseRepository<CustomForm, String> {
    List<CustomForm> findByCode(String code);
}
