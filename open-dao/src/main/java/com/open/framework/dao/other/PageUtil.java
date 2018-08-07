package com.open.framework.dao.other;


import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.RequestHolder;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.commmon.web.PageBean;
import javafx.scene.control.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;


/**
 * 仅仅只是对size进行了封装，太过简单
 */
public class PageUtil {


    public static final int size = 10;

    public static Pageable getPage(int page, int size, Sort sort) {
        if (sort == null) return PageRequest.of(page - 1, size);
        return PageRequest.of(page - 1, size, sort);
    }

    public static Pageable getPage(int page) {
        return getPage(page, size, null);
    }

    public static Pageable getPage(int page, int size) {
        return getPage(page, size, null);
    }

    public static Pageable getPage(int page, Sort sort) {
        return getPage(page, size, sort);
    }

    public static Pageable getPage(PageBean pageBean, String sorts) {
        pageBean = getPagebean(pageBean);
        Integer pageSize = pageBean.getPage();
        Integer page = pageBean.getPageSize();
        if (pageSize > 0) {
            if (StringUtil.isNotEmpty(sorts)) {
                return getPage(page, pageSize, SortUtil.sort(sorts.split(",")));
            } else {
                return getPage(page, pageSize);
            }
        } else {
            return null;
        }
    }

    public static PageBean getPagebean(PageBean pageBean) {
        Integer pageSize = 0;
        Integer page = 0;
        HttpServletRequest req = RequestHolder.getRequest();
        // 如果有值是get方式请求
        String pageSizeStr = req.getParameter(BaseConstant.PAGE_SIZE);
        String pageStr = req.getParameter(BaseConstant.PAGE);
        if (StringUtil.isEmpty(pageSizeStr) && StringUtil.isEmpty(pageStr)) {
            if (!StringUtil.equalsIgnoreCase("GET", RequestHolder.getRequest().getMethod()) && null != pageBean) {
                pageSize = pageBean.getPageSize() == 0 ? 10 : pageBean.getPageSize();
                page = pageBean.getPage() == 0 ? 1 : pageBean.getPage();
            } else {
                pageSize = 10;
                page = 1;
            }
        } else {
            pageSize = Integer.valueOf(pageSizeStr);
            page = Integer.valueOf(pageStr);
        }
        pageBean.setPage(page);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }
}