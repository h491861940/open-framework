package com.open.framework.supports.customform.service;


import com.open.framework.supports.customform.dto.CustomFormCommonDTO;

import java.util.List;

/**
 * @Auther: hsj
 * @Date: 2019/3/26 15:08
 * @Description:
 */
public interface CustomFormCommonService {
    Object save(CustomFormCommonDTO customFormCommonDTO);

    void modify(CustomFormCommonDTO customFormCommonDTO);

    Object findById(String gid);

    Object findAll();

    void deleteByIds(List<String> gids);
}
