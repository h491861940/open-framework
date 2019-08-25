package com.open.framework.file.service.impl;

import com.open.framework.commmon.utils.ExcelUtil;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.RequestHolder;
import com.open.framework.commmon.web.ExportData;
import com.open.framework.file.service.ExportService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


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
        //因为字段带_f表示格式化,所以需要重新组建列,用于转换
        String[] newColumns=new String[columns.length];
        List datas=new ArrayList();
        if(null!=exportData){
            // 如果有导出记录行数，则执行转换
            if (listDatas.size() > 0)
            {
                for(Object obj:listDatas){
                    String json=JsonUtil.toJSONString(obj);
                    //转成map
                    Map mapTemp=JsonUtil.strToMap(json);
                    //新的导出的map,后续可以做转换,或者格式化等
                    Map newMap=new HashMap();
                    for (int i=0;i<columns.length;i++)
                    {
                        String col=columns[i];
                        String[] cols=col.split("_");
                        //判断是否有转换的需求
                        if(cols.length==2){
                            Object value=mapTemp.get(cols[0]);
                            //获取转换值
                            Object valueFormat=ExcelUtil.formatMap.get(value);
                            newColumns[i]=cols[0];
                            //转换值为空,则存放原值
                            if(null==valueFormat){
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
                //默认导出的文件名
                String fileName = "export";
                if(exportData.getFileName()!=null && !"".equals(exportData.getFileName())){
                    fileName = exportData.getFileName();
                }
                if(!(fileName.endsWith(".xls") || fileName.endsWith(".XLS"))){
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
