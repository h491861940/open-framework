package com.open.framework.file.easypoi;


import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: https://opensource.afterturn.cn/doc/easypoi.html#90103
 * 使用easypoi,测试导入导出,教程地址
 * @author: hsj
 * @date: 2019-08-24 17:26:41
 */
@RestController
@RequestMapping("/easypoiTest")
public class EasypoiTest {

    @GetMapping()
    public String download(ModelMap map) {
        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        ExportParams params = new ExportParams();
        map.put(NormalExcelConstants.DATA_LIST, list);
        map.put(NormalExcelConstants.CLASS, MsgClient.class);
        map.put(NormalExcelConstants.PARAMS, params);
        return NormalExcelConstants.EASYPOI_EXCEL_VIEW;

    }

    /**如果上面的方法不行,可以使用下面的用法
     * 同样的效果,只不过是直接问输出了,不经过view了
     * @param map
     * @param request
     * @param response
     */

    @GetMapping("load")
    public void downloadByPoiBaseView(ModelMap map, HttpServletRequest request,
                                      HttpServletResponse response) {
        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        map.put(NormalExcelConstants.DATA_LIST, list);//数据
        map.put(NormalExcelConstants.CLASS, MsgClient.class);//注解集合
        map.put(NormalExcelConstants.PARAMS, params);//参数
        map.put(NormalExcelConstants.FILE_NAME, "我的");//文件名
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }
    /**
     * @description:  easypoi的导入
     * @author: hsj
     * @date: 2019-08-24 23:42:05
     */
    @PostMapping("importExcel")
    public String importExcel(@RequestParam(value="file",required = false) MultipartFile file) throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        List<MsgClient> list = ExcelImportUtil.importExcel(file.getInputStream(),
                MsgClient.class, params);
        System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
        return  "success";
    }
}
