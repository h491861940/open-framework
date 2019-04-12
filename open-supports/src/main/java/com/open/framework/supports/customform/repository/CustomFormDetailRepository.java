package com.open.framework.supports.customform.repository;


import com.open.framework.dao.repository.base.BaseRepository;
import com.open.framework.supports.customform.entity.CustomFormDetail;

import java.util.List;


/**
 * @Author hsj
 * @Description 表单明细的数据库维护
 * @Date  2019-03-25 16:11:49
 **/
public interface CustomFormDetailRepository extends BaseRepository<CustomFormDetail, String> {
    List<CustomFormDetail> findByParentGidAndParentType(String parentGid, Integer ParentType);
    int deleteByParentGidAndParentType(String parentGid, Integer ParentType);
}
