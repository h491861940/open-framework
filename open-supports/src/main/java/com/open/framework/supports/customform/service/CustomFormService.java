package com.open.framework.supports.customform.service;

import com.open.framework.supports.customform.entity.CustomForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @Auther: hsj
 * @Date: 2019/3/26 10:59
 * @Description: 自定义表单接口
 */
public interface CustomFormService {
    /**
     * 保存表单
     * @param customForm
     * @return
     */
    String save(CustomForm customForm);

    /**
     * 动态修改表单
     * @param customForm
     */
    void dynamicModify(CustomForm customForm);

    /**
     * 非动态,先删除,后新增,这种修改不支持多次发布
     * @param customForm
     */
    void modify(CustomForm customForm);
    /**
     * 根据gid查询明细
     * @param gid
     * @return
     */
    CustomForm findById(String gid);

    /**
     * 查询全部,用于列表展示,所以只查询最外层即可
     * @return
     */
    List<CustomForm> findAll();

    /**
     * 根据gid生成菜单
     * @param gid
     * @return
     */
    String generateMenu(String gid);

    /**
     * 发布表单
     * @param gid
     * @throws Exception
     */
    void publishForm(String gid) throws Exception;
    /**
     * 获取字段类型下拉框
     * @return
     **/
    List getFieldType();

    Page<CustomForm> findAll(CustomForm condition, PageRequest pageRequest);
}
