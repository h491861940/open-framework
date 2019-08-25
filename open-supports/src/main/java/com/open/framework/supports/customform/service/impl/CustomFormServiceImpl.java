package com.open.framework.supports.customform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.open.framework.commmon.exceptions.BusinessException;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.supports.customform.CustomFormDbUtil;
import com.open.framework.supports.customform.entity.CustomForm;
import com.open.framework.supports.customform.entity.CustomFormChild;
import com.open.framework.supports.customform.entity.CustomFormDetail;
import com.open.framework.supports.customform.repository.CustomFormChildRepository;
import com.open.framework.supports.customform.repository.CustomFormDetailRepository;
import com.open.framework.supports.customform.repository.CustomFormRepository;
import com.open.framework.supports.customform.service.CustomFormService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: hsj
 * @Date: 2019/3/26 10:59
 * @Description: 自定义表单实现接口
 */
@Service
@Transactional
public class CustomFormServiceImpl  implements CustomFormService {
    private static final String POST_URL="test";
    @Autowired
    private CustomFormRepository customFormRepository;
    @Autowired
    private CustomFormChildRepository customFormChildRepository;
    @Autowired
    private CustomFormDetailRepository customFormDetailRepository;
    @Autowired(required = false)
    private RestTemplate restTemplate;
    /**
     * @Author hsj
     * @Description 获取新gid
     * @Date  2019-03-26 14:20:12
     * @return String
     **/
    private String getNewGid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @Author hsj
     * @Description 获取新code
     * @Date  2019-03-26 14:19:59
     * @return String
     **/
    private String getNewCode(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");
        String dateString = formatter.format(new Date());
        String code="CustomForm"+dateString;
        return code;
    }
    @Override
    public String save(CustomForm customForm) {
        List<CustomFormDetail> customFormDetailList = new ArrayList<>();
        List<CustomFormChild> customFormChildList = new ArrayList<>();
        if(StringUtil.isNotEmpty(customForm.getCode())){
            List exitsList=customFormRepository.findByCode(customForm.getCode());
            if(exitsList.size()>0){
                throw new BusinessException(customForm.getCode()+"已经存在无法重复添加");
            }
        }

        customForm.setGid(getNewGid());
        //发布状态
        customForm.setPublishState(0);
        customForm.setCreateTime(new Date());
        //判断请求地址是否为空,为空填写默认地址
        if(StringUtil.isEmpty(customForm.getPostUrl())){
            customForm.setPostUrl(POST_URL);
        }
        //判断是否有code,没有用规则生成
        if(StringUtil.isEmpty(customForm.getCode())){
            customForm.setCode(getNewCode());
        }
        //设置自定义表单主体对象
        if(CollectionUtils.isNotEmpty(customForm.getFields())){
            customForm.getFields().stream().forEach((x)-> {
                x.setGid(getNewGid());
                customFormDetailList.add(x.setParentType(1).setParentGid(customForm.getGid()).setCudState(1).setCreateTime(new Date()));
            });
        }
        //自定义表单子表对象
        if(CollectionUtils.isNotEmpty(customForm.getChilds())){
            for (int i = 0; i <customForm.getChilds().size(); i++) {
                CustomFormChild customFormChild=customForm.getChilds().get(i);
                customFormChild.setGid(getNewGid());
                customFormChild.setCustomFormGid(customForm.getGid()).setCudState(1);
                if(StringUtil.isEmpty(customFormChild.getCode())){
                    customFormChild.setCode(customForm.getCode()+i);
                }
                //子表对象的属性
                if(CollectionUtils.isNotEmpty(customFormChild.getFields())){
                    customFormChild.getFields().stream().forEach((x)-> {
                        x.setGid(getNewGid());
                        customFormDetailList.add(x.setParentType(2).setParentGid(customFormChild.getGid()).setCudState(1).setCreateTime(new Date()));
                    });
                }
                customFormChildList.add(customFormChild);
            }
        }
        customFormRepository.save(customForm);
        customFormChildRepository.saveAll(customFormChildList);
        customFormDetailRepository.saveAll(customFormDetailList);
        return customForm.getGid();
    }

    @Override
    public void dynamicModify(CustomForm customForm) {
        List<CustomFormDetail> customFormDetailList = new ArrayList<>();
        List<CustomFormChild> customFormChildList = new ArrayList<>();
        Date date=new Date();
        //每次修改,修改发布状态
        customForm.setPublishState(0);
        if(StringUtil.isEmpty(customForm.getPostUrl())){
            customForm.setPostUrl(POST_URL);
        }
        //自定义表单,判断属性的状态,做不同的处理
        if(CollectionUtils.isNotEmpty(customForm.getFields())){
            customForm.getFields().stream().forEach((x)-> {
                if(StringUtil.isEmpty(x.getGid())){
                    x.setGid(getNewGid());
                    customFormDetailList.add(x.setParentType(1).setParentGid(customForm.getGid()).setCudState(1).setCreateTime(date));
                }else if(x.getCudState()==3){
                    customFormDetailRepository.deleteById(x.getGid());
                }else{
                    customFormDetailList.add(x.setModifyTime(date));
                }
            });
        }
        if(CollectionUtils.isNotEmpty(customForm.getChilds())){
            for (int i = 0; i <customForm.getChilds().size() ; i++) {
                CustomFormChild customFormChild=customForm.getChilds().get(i);
                if(StringUtil.isEmpty(customFormChild.getGid())){
                    customFormChild.setGid(getNewGid());
                    customFormChild.setCustomFormGid(customForm.getGid()).setCudState(1);
                    if(StringUtil.isEmpty(customFormChild.getCode())){
                        customFormChild.setCode(customForm.getCode()+i);
                    }
                    if(CollectionUtils.isNotEmpty(customFormChild.getFields())){
                        customFormChild.getFields().stream().forEach((x)-> {
                            x.setGid(getNewGid());
                            customFormDetailList.add(x.setParentType(2).setParentGid(customFormChild.getGid()).setCudState(1).setCreateTime(date));
                        });
                    }
                    customFormChildList.add(customFormChild);
                }else if(customFormChild.getCudState()==3){
                    customFormChildRepository.deleteById(customFormChild.getGid());
                    customFormDetailRepository.deleteByParentGidAndParentType(customFormChild.getGid(),2);
                }else {
                    customFormChildList.add(customFormChild);
                    if(CollectionUtils.isNotEmpty(customFormChild.getFields())){
                        customFormChild.getFields().stream().forEach((x)-> {
                            if(StringUtil.isEmpty(x.getGid())){
                                x.setGid(getNewGid());
                                customFormDetailList.add(x.setParentType(2).setParentGid(customFormChild.getGid()).setCudState(1).setCreateTime(date));
                            }else if(x.getCudState()==3){
                                customFormDetailRepository.deleteById(x.getGid());
                            }else{
                                customFormDetailList.add(x.setModifyTime(date));
                            }
                        });
                    }
                }
            }
        }
        customFormRepository.save(customForm);
        customFormChildRepository.saveAll(customFormChildList);
        customFormDetailRepository.saveAll(customFormDetailList);
    }
    @Override
    public void modify(CustomForm customForm) {
        List<CustomFormDetail> customFormDetailList = new ArrayList<>();
        List<CustomFormChild> customFormChildList = new ArrayList<>();
        Date date=new Date();
        if(CollectionUtils.isNotEmpty(customForm.getChilds())){
            for (int i = 0; i <customForm.getChilds().size() ; i++) {
                CustomFormChild customFormChild=customForm.getChilds().get(i);
                customFormDetailRepository.deleteByParentGidAndParentType(customFormChild.getGid(),2);
            }
        }
        customFormDetailRepository.deleteByParentGidAndParentType(customForm.getGid(),1);
        customFormChildRepository.deleteByCustomFormGid(customForm.getGid());
        //设置自定义表单主体对象
        if(CollectionUtils.isNotEmpty(customForm.getFields())){
            customForm.getFields().stream().forEach((x)-> {
                x.setGid(getNewGid());
                customFormDetailList.add(x.setParentType(1).setParentGid(customForm.getGid()).setCudState(1).setCreateTime(date));
            });
        }
        //自定义表单子表对象
        if(CollectionUtils.isNotEmpty(customForm.getChilds())){
            for (int i = 0; i <customForm.getChilds().size() ; i++) {
                CustomFormChild customFormChild=customForm.getChilds().get(i);
                customFormChild.setGid(getNewGid());
                customFormChild.setCustomFormGid(customForm.getGid()).setCudState(1).setCreateTime(date);
                if(StringUtil.isEmpty(customFormChild.getCode())){
                    customFormChild.setCode(customForm.getCode()+i);
                }
                //子表对象的属性啊
                if(CollectionUtils.isNotEmpty(customFormChild.getFields())){
                    customFormChild.getFields().stream().forEach((x)-> {
                        x.setGid(getNewGid());
                        customFormDetailList.add(x.setParentType(2).setParentGid(customFormChild.getGid()).setCudState(1).setCreateTime(date));
                    });
                }
                customFormChildList.add(customFormChild);
            }
        }
        customFormRepository.save(customForm);
        customFormChildRepository.saveAll(customFormChildList);
        customFormDetailRepository.saveAll(customFormDetailList);
    }
    @Override
    public CustomForm findById(String gid) {
        CustomForm customForm=customFormRepository.getOne(gid);
        customForm.setFields(customFormDetailRepository.findByParentGidAndParentType(gid,1));
        List<CustomFormChild> customFormChildList=customFormChildRepository.findByCustomFormGid(gid);
        if(CollectionUtils.isNotEmpty(customFormChildList)){
            customFormChildList.forEach(x->{
                x.setFields(customFormDetailRepository.findByParentGidAndParentType(x.getGid(),2));
            });
        }
        customForm.setChilds(customFormChildList);
        return customForm;
    }

    @Override
    public List<CustomForm> findAll() {
        return customFormRepository.findAll();
    }
    @Override
    public Page<CustomForm> findAll(CustomForm condition, PageRequest pageRequest) {
        return this.customFormRepository.findAll(Example.of(condition), pageRequest);
    }
    @Override
    public String generateMenu(String gid) {
        CustomForm customForm=customFormRepository.getOne(gid);
        //调用生成菜单接口restTemplate applicationGid:6673a60821fb4205a01f9a51cdcdecbf
        String url="http://192.168.138.67:8080/ddd-modeling/gateway/modeling/modelingFunctionService/add";
        JSONObject postData = new JSONObject();
        postData.put("applicationGid", customForm.getApplicationGid());
        postData.put("code", customForm.getCode());
        postData.put("name", customForm.getName());
        postData.put("virtual", true);

        JSONObject json = restTemplate.postForEntity(url, postData.toJSONString(), JSONObject.class).getBody();
        Boolean success= json.getBoolean("success");
        String menuUrl="";
        if(success){
            String menuGid=json.getJSONObject("data").getString("gid");
            menuUrl=menuGid;
            if(StringUtil.isNotEmpty(menuGid)){
                String saveUrl="http://192.168.138.67:8080/ddd-modeling/gateway/modeling/modelingFunctionService/save";
                JSONObject postDataSave = new JSONObject();
                postDataSave.put("ids", new String[]{menuGid});
                postDataSave.put("commitMessage", "自定义表单创建");

                JSONObject jsonSave = restTemplate.postForEntity(saveUrl, postDataSave.toJSONString(), JSONObject.class).getBody();
                Boolean successSave= jsonSave.getBoolean("success");
                if(!successSave){
                    throw new BusinessException("提交菜单失败"+jsonSave.getString("error"));
                }
            }
        }else {
            throw new BusinessException("生成菜单失败"+json.getString("error"));
        }
        if(StringUtil.isNotEmpty(menuUrl)){
            customForm.setMenuUrl(menuUrl);
        }
        return menuUrl;
    }

    @Override
    public void publishForm(String gid) throws Exception {
        CustomForm customForm= findById(gid);
        if(1==customForm.getPublishState()){
            throw new BusinessException("已经发布,无法重复发布");
        }
        customForm.setPublishState(1);
        customForm.setModifyTime(new Date());
        CustomFormDbUtil.generateTable(customForm);
        customFormRepository.save(customForm);
        customFormChildRepository.saveAll(customForm.getChilds());
    }

    @Override
    public List getFieldType() {
        return CustomFormDbUtil.getFieldType();
    }


}
