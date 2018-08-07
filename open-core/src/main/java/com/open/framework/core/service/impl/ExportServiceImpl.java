package com.open.framework.core.service.impl;

import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.utils.ExcelUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.RequestHolder;
import com.open.framework.commmon.web.ExportData;
import com.open.framework.core.service.ExportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @Auther: hsj
 * @Date: 2018/7/22 17:14
 * @Description:
 */
@Service
public class ExportServiceImpl implements ExportService {
    @Override
    public void export(ExportData exportData) {
        List listDatas=exportData.getDatas();
        String[] colNames = exportData.getColumnNames();
        String[] columns = exportData.getColumns();
        if(null==colNames || null==columns){
            return;
        }
        String[] newColumns=newColumns =new String[columns.length];//因为字段带_f表示格式化,所以需要重新组建列,用于转换
        List datas=new ArrayList();
        if(null!=exportData){
            if (listDatas.size() > 0) // 如果有导出记录行数，则执行转换
            {
                for(Object obj:listDatas){
                    String json=JsonUtil.toJSONString(obj);
                    Map mapTemp=JsonUtil.strToMap(json);//转成map
                    Map newMap=new HashMap();//新的导出的map,后续可以做转换,或者格式化等
                    for (int i=0;i<columns.length;i++)
                    {
                        String col=columns[i];
                        String[] cols=col.split("_");
                        if(cols.length==2){//判断是否有转换的需求
                            Object value=mapTemp.get(cols[0]);
                            Object valueFormat=BaseConstant.formatMap.get(value);//获取转换值
                            newColumns[i]=cols[0];
                            if(null==valueFormat){//转换值为空,则存放原值
                                newMap.put(cols[0],value);
                            }else{
                                newMap.put(cols[0],valueFormat);
                            }
                        }else{
                            newColumns[i]=col;
                            newMap.put(col,mapTemp.get(col));
                        }

                    }
                    datas.add(newMap);
                }
            }
            Workbook workbook = ExcelUtil.getHSSFWorkbook(Arrays.asList(newColumns), Arrays.asList(colNames), datas);
            try {
                String fileName = "export";//默认导出的文件名
                if(exportData.getFileName()!=null && !"".equals(exportData.getFileName())){
                    fileName = exportData.getFileName();
                }
                if(!(fileName.endsWith(".xls")||fileName.endsWith(".XLS"))){
                    fileName+=".xls";
                }
                HttpServletResponse response=RequestHolder.getResponse();
                OutputStream output=RequestHolder.getResponse().getOutputStream();
                response.reset();
                response.setHeader("Content-disposition", "attachment; filename="+fileName);
                response.setContentType("application/msexcel");
                workbook.write(output);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        }
    }
}
