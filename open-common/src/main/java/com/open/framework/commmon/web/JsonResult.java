package com.open.framework.commmon.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.open.framework.commmon.BaseConstant;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * http请求返回的最外层对象
 */
@Data
public class JsonResult<T> {

    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体的内容.
     */
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageBean pageBean;
    /**
     * 把对象放到data返回
     * @param object
     * @return
     */
    public static JsonResult success(Object object) {
        JsonResult JsonResult = new JsonResult();
        JsonResult.setCode(BaseConstant.SUCCESS_CODE);
        JsonResult.setMsg(BaseConstant.SUCCESS);
        JsonResult.setData(object);
        return JsonResult;
    }

    /**
     * 返回分页信息,自己组建好PageBean
     * @param pageBean
     * @return
     */
    public static JsonResult successPage(PageBean pageBean) {
        JsonResult JsonResult = new JsonResult();
        JsonResult.setCode(BaseConstant.SUCCESS_CODE);
        JsonResult.setMsg(BaseConstant.SUCCESS);
        JsonResult.setPageBean(pageBean);
        return JsonResult;
    }

    /**
     * 传入Page对象,组建返回的分页信息,为了和前端对应pageBean.setPage会-1,因为Page第几页数是从0开始
     * @param page
     * @return
     */
    public static JsonResult successPage(Page page) {
        PageBean pageBean = new PageBean();
        pageBean.setNumberOfElements(page.getNumberOfElements());
        pageBean.setPage(page.getNumber());
        pageBean.setPageSize(page.getSize());
        pageBean.setRows(page.getContent());
        pageBean.setCount(page.getTotalElements());
        pageBean.setTotalElements(page.getTotalElements());
        pageBean.setTotalPages(page.getTotalPages());
        return successPage(pageBean);
    }

    /**
     * 返回 成功信息
     * @return
     */
    public static JsonResult success() {
        return success(null);
    }

    /**
     *
     * @param code 异常编码
     * @param msg 异常信息
     * @return
     */
    public static JsonResult error(Integer code, String msg) {
        JsonResult JsonResult = new JsonResult();
        JsonResult.setCode(code);
        JsonResult.setMsg(msg);
        return JsonResult;
    }
    /**
     *
     * @param code 异常编码
     * @param msg 异常信息
     * @return
     */
    public static JsonResult error(String msg) {
        JsonResult JsonResult = new JsonResult();
        JsonResult.setCode(-1);
        JsonResult.setMsg(msg);
        return JsonResult;
    }
}