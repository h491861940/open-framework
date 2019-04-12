package com.open.framework.supports.customform.repository;


import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.supports.customform.entity.CustomFormChild;

import java.util.List;


/**
 * @Author hsj
 * @Description 表单子表的数据库维护
 * @Date  2019-03-25 16:11:49
 **/
public interface CustomFormChildRepository extends BaseRepository<CustomFormChild, String> {
    List<CustomFormChild> findByCustomFormGid(String customFormGid);
    int deleteByCustomFormGid(String customFormGid);
}
