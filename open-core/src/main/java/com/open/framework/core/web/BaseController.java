package com.open.framework.core.web;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.exceptions.PlatformException;
import com.open.framework.commmon.utils.*;
import com.open.framework.commmon.web.ExportData;
import com.open.framework.commmon.web.JsonResult;
import com.open.framework.commmon.web.PageBean;
import com.open.framework.commmon.web.QueryParam;
import com.open.framework.core.service.BaseService;
import com.open.framework.dao.model.BaseDTO;
import com.open.framework.dao.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/base")
public class BaseController<T, D> {
    @Autowired
    BaseService<T, D> baseService;
    @Autowired
    HttpServletRequest request;

    /**
     * 获取泛型dto或者全类名实例化dto对象
     */
    private Class dtoClass;
    private Class entityClass;
    /**
     * 头信息里面的dot全类名
     */
    private String className;

    /**
     * @return
     * @Author hsj
     * @Description 每个方法执行前的处理方法, 获取dto
     * @Date 2018-07-11 13:23:39
     * @Param
     **/
    @ModelAttribute
    public void bindInfo() {
        className = request.getHeader("className");
        dtoClass = ClassUtil.getDtoClass(this.getClass());
        if (null == dtoClass) {
            if (StringUtil.isEmpty(className)) {
                throw new PlatformException("头信息里面没有className,无法转换对象.");
            }
            try {
                dtoClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new PlatformException("没有找见对应的类:className:" + className);
            }
        }
    }

    /**
     * @return
     * @Author hsj
     * @Description json格式的dto, 自动转换成dto, 然后保存, 保存时候根据dto找见对应的实体
     * @Date 2018-07-11 13:22:54
     * @Param dto:
     **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResult save(@RequestBody D dto) {
        T t = null;
        if (dto instanceof Map) {
            System.out.println("我是没有子类继承的");
            Object dtoTemp = JsonUtil.mapToBean((Map) dto, dtoClass);
            t = baseService.saveDto((D) dtoTemp);
        } else if (dto instanceof BaseDTO) {
            System.out.println("我是有子类继承带泛型的");
            t = baseService.saveDto(dto);
        }
        return JsonResultUtil.success(((BaseEntity) t).getGid());
    }

    /**
     * @return
     * @Author hsj
     * @Description 根据id集合, 删除对象
     * @Date 2018-07-11 13:17:45
     * @Param ids
     **/
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public JsonResult delete(@RequestBody String[] gids) {
        if (null != gids && gids.length > 0) {
            baseService.deleteBatchDto(dtoClass, Arrays.asList(gids));
        }
        return JsonResultUtil.success();
    }

    /**
     * @return
     * @Author hsj
     * @Description 根据id查询对象
     * @Date 2018-07-11 13:17:11
     * @Param id
     **/
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public JsonResult findById(@RequestParam String gid) {
        D dto = baseService.getDto(dtoClass, gid);
        return JsonResultUtil.success(dto);
    }

    /**
     * @return
     * @Author hsj
     * @Description 通用的查询方法, 提供查询
     * @Date 2018-07-11 13:14:11
     * @Param QueryParam 分页和条件对象
     * 条件例子
     * {"showCount":true,"pageBean":{"pageSize":2,"page":7},"specOpers":[{"key":"address","value":"1","oper":"eq"},
     * [{"key":"address","value":"1","oper":"like","join":"or"},{"key":"address","value":"1","oper":"eq"}],
     * {"key":"address","value":"1","oper":"eq"},[{"key":"address","value":"1","oper":"like","join":"and"},
     * {"key":"address","value":"1","oper":"like"},{"key":"address","value":"1","oper":"eq"}]]}
     **/
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public JsonResult query(@RequestBody QueryParam queryParam) {
        Object obj = baseService.query(dtoClass, queryParam);
        if (obj instanceof PageBean) {
            return JsonResultUtil.successPage((PageBean) obj);
        } else {
            return JsonResultUtil.success(obj);
        }
    }


    @RequestMapping(value = "/export", method = RequestMethod.POST)
    /**
     * @Author hsj
     * @Description
     * @Date 2018-07-22 22:09:05
     * @param exportData 导出的条件和字段,列名
     * @return void
     * 列子
     * columns里面的字段带_f说明需要转换,则去BaseConstant.formatMap里面获取要转换的值
     * {"columnNames":["名字","年龄"],"columns":["name_f","age"],"queryParam":{"specOpers":[{"key":"age","value":23,
     * "oper":"eq"}],"pageBean":{"page":1,"pageSize":5}}}
     * @param exportData
     */
    public void export(@RequestBody ExportData exportData) {
        BaseConstant.formatMap.put("qwert", "qqqq");
        baseService.export(dtoClass, exportData);
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(@RequestParam(required = false) String fileName, @RequestParam String[] columnNames,
                       @RequestParam String[] columns) {
        ExportData exportData = new ExportData(fileName, columnNames, columns);
        baseService.export(dtoClass, exportData);
    }
}
