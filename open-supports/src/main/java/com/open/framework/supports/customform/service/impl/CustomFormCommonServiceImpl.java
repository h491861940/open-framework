package com.open.framework.supports.customform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.framework.supports.customform.CustomFormDbUtil;
import com.open.framework.supports.customform.dto.CustomFormCommonDTO;
import com.open.framework.supports.customform.entity.CustomForm;
import com.open.framework.supports.customform.service.CustomFormCommonService;
import com.open.framework.supports.customform.service.CustomFormService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: hsj
 * @Date: 2019/3/26 15:09
 * @Description:
 */
@Service
public class CustomFormCommonServiceImpl implements CustomFormCommonService {
    @Autowired
    private CustomFormService customFormService;

    @Override
    public Object save(CustomFormCommonDTO customFormCommonDTO) {
        String formId=customFormCommonDTO.getFormId();
        CustomForm customForm=customFormService.findById(formId);
        JSONObject data=customFormCommonDTO.getData();
        Map mainTable=new HashMap();
        if(CollectionUtils.isNotEmpty(customForm.getFields())){
            customForm.getFields().forEach(x->{
                mainTable.put(x.getCode(),data.get(x.getCode()));
            });
        }
        CustomFormDbUtil.insert();
        return null;
    }

    @Override
    public void modify(CustomFormCommonDTO customFormCommonDTO) {

    }

    @Override
    public Object findById(String gid) {
        return null;
    }

    @Override
    public Object findAll() {
        return null;
    }

    @Override
    public void deleteByIds(List<String> gids) {

    }
}
