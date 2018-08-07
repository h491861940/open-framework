package com.open.framework.file.service.impl;

import com.open.framework.file.dto.ExportData;
import com.open.framework.file.service.ExportService;
import com.open.framework.file.utils.ExcelUtils;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

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
        HashMap<String,Object> dataMap = new HashMap<>();
        String[] rowsName = new String[]{"时间","发言人","类型","消息"};
        exportData.setColumn(rowsName);
        List<Map> datas = new ArrayList<Map>();
        String[] colNames = exportData.getColumnNames();
        String[] columns = exportData.getColumn();
        for(int i=0;i<30;i++){
            dataMap.put("datetime", "2017-12-13 10:43:00");
            dataMap.put("person", "张翠山");
            dataMap.put("type", "文本");
            dataMap.put("content", "工作一定要认真，态度要端正，作风要优良，行事要效率，力争打造一个完美的产品出来。");
            datas.add(dataMap);
        }
        if(null!=exportData){
            if (datas.size() > 0) // 如果有导出记录行数，则执行转换
            {
                for (String colName : colNames)
                {
                    if (datas.get(0).containsKey(colName)) // 只有导出的列包含此列时才执行循环
                    {

                    }
                }
            }
            Workbook workbook = ExcelUtils.getHSSFWorkbook(Arrays.asList(columns), Arrays.asList(colNames), datas);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                //workbook.write(os);
                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                String fileName = "export";//默认导出的文件名
                if(exportData.getFileName()!=null && !"".equals(exportData.getFileName())){
                    fileName = exportData.getFileName();
                }
                if(!(fileName.endsWith(".xls")||fileName.endsWith(".XLS"))){
                    fileName+=".xls";
                }
                //StreamDataMode streamData = new StreamDataMode(fileName,is,"application/vnd.ms-excel;charset=UTF-8");
                FileOutputStream fos = new FileOutputStream(new File("E:\\sheet\\11.xls"));
                workbook.write(fos);
                workbook.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
        }
    }
}
