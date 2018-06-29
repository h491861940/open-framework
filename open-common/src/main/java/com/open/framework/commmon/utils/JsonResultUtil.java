package com.open.framework.commmon.utils;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.PageBean;
import org.springframework.data.domain.Page;

public class JsonResultUtil {
    /**
     * 把对象放到data返回
     * @param object
     * @return
     */
    public static JsonResult success(Object object) {
        JsonResult JsonResult = new JsonResult();
        JsonResult.setCode(BaseConstant.RESULT_SUCCESS);
        JsonResult.setMsg(BaseConstant.RESULT_SUCCESS_MSG);
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
        JsonResult.setCode(BaseConstant.RESULT_SUCCESS);
        JsonResult.setMsg(BaseConstant.RESULT_SUCCESS_MSG);
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
}