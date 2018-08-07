package com.open.framework.commmon.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelUtil
{
	//SXSSFWorkbook类型，当内存中保留的最大条数。-1表示全部放在内存中。
	public static final int SXSSF_WORKBOOK_UNFLUSH_COUNT = 128;
	/**
	 * 拷贝Sheet
	 * 
	 * @param fromUrl
	 *            源文件
	 * @param toUrl
	 *            生成后文件
	 * @param num
	 *            拷贝数量
	 */
	public static void copySheet(String fromUrl, String toUrl, int num)
	{
		FileInputStream in = null;
		HSSFWorkbook workbook = null;

		try
		{
			in = new FileInputStream(fromUrl);
			POIFSFileSystem fs = new POIFSFileSystem(in);
			workbook = new HSSFWorkbook(fs);
		} catch (IOException e)
		{
			System.out.println(e.toString());
		} finally
		{
			try
			{
				in.close();
			} catch (IOException e)
			{
				System.out.println(e.toString());
			}
		}
		workbook.setSheetName(0, "s" + 0);
		for (int i = 0; i < num; i++)
		{
			workbook.cloneSheet(i);
			int no = i + 1;
			workbook.setSheetName(no, "s" + no);

		}

		FileOutputStream out = null;
		try
		{
			out = new FileOutputStream(toUrl);
			workbook.write(out);
		} catch (IOException e)
		{
			System.out.println(e.toString());
		} finally
		{
			try
			{
				out.close();
			} catch (IOException e)
			{
				System.out.println(e.toString());
			}
		}
	}

	/**
	 * POI获取模板文件单元格样式
	 * 
	 * @param sheet
	 * @param rownum
	 * @param column
	 * @return
	 */
	public static CellStyle getTempalteStyle(Sheet sheet, int rownum, int column)
	{
		return sheet.getRow(rownum).getCell(column).getCellStyle();
	}

	/**
	 * 重写POI创建单元格方法
	 * 
	 * @param sheet
	 * @param rownum
	 * @param column
	 * @param style
	 * @param value
	 * @return
	 */
	public static Cell createCell(Sheet sheet, int rownum, int column, CellStyle style, String value)
	{
		Row row = sheet.getRow(rownum) == null ? sheet.createRow(rownum) : sheet.getRow(rownum);
		Cell cell = row.getCell(column) == null ? row.createCell(column) : row.getCell(column);
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}

	/**
	 * 重写POI合并单元格方法
	 * 
	 * @param sheet
	 * @param rowFrom
	 * @param colFrom
	 * @param rowTo
	 * @param colTo
	 */
	public static void mergeCells(Sheet sheet, int rowFrom, int colFrom, int rowTo, int colTo)
	{
		CellRangeAddress region = new CellRangeAddress(rowFrom, rowTo, colFrom, colTo);
		sheet.addMergedRegion(region);
	}

	/**
	 * 返回工作薄对象
	 * 
	 * @param fieldName
	 *            自定义需要显示的列，对应Map中的Key
	 * @param fieldTitle
	 *            显示的标题，通过index对应fieldName
	 * @param data
	 *            数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HSSFWorkbook getHSSFWorkbook(String sheetName, List<String> fieldName, List<String> fieldTitle, List<Map> data)
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet(sheetName);
		CellStyle style = workbook.createCellStyle();
		// 建立标题
		for (int i = 0; i < fieldTitle.size(); i++)
		{
			ExcelUtil.createCell(sheet, 0, i, style, fieldTitle.get(i));
		}
		// 遍历数据
		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			for (int j = 0; j < fieldName.size(); j++)
			{
				String content = map.get(fieldName.get(j))==null?null:String.valueOf(map.get(fieldName.get(j)));
				ExcelUtil.createCell(sheet, i + 1, j, style, content);
			}
		}
		return workbook;
	}

	@SuppressWarnings("rawtypes")
    public static HSSFWorkbook getHSSFWorkbook(List<String> fieldName, List<String> fieldTitle, List<Map> data)
	{
		return getHSSFWorkbook("Sheet0", fieldName, fieldTitle, data);
	}
	/**
	 * 返回工作薄对象
	 * 
	 * @param fieldName
	 *            列名数组，可以为空
	 * @param data
	 *            数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HSSFWorkbook getHSSFWorkbook(List<String> fieldName, List<Map> data)
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();

		CellStyle style = workbook.createCellStyle();

		int flag = 0;
		for (int i = 0; i < fieldName.size(); i++)
		{
			ExcelUtil.createCell(sheet, 0, i, style, fieldName.get(i));
			flag = 1;
		}

		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			List temp = new ArrayList(map.keySet());

			for (int j = 0; j < temp.size(); j++)
			{
				String content = (String) map.get(temp.get(j));
				ExcelUtil.createCell(sheet, i + flag, j, style, content);
			}
		}

		return workbook;
	}

	
	/**
	 * 返回工作薄对象
	 * 
	 * @param fieldName
	 *            自定义需要显示的列，对应Map中的Key
	 * @param fieldTitle
	 *            显示的标题，通过index对应fieldName
	 * @param data
	 *            数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static SXSSFWorkbook getSXSSFWorkbook(SXSSFWorkbook workbook,String sheetName, List<String> fieldName, List<String> fieldTitle, List<Map> data)
	{
		if(workbook==null)
		{
		 workbook = new SXSSFWorkbook(SXSSF_WORKBOOK_UNFLUSH_COUNT);
		}
//		大数据量时，启用临时文件压缩。
//		if(data.size() > SXSSF_WORKBOOK_UNFLUSH_COUNT * 10){
//			workbook.setCompressTempFiles(true);
//		}
		Sheet sheet = workbook.createSheet(sheetName);
		CellStyle style = workbook.createCellStyle();
		// 建立标题
		for (int i = 0; i < fieldTitle.size(); i++)
		{
			ExcelUtil.createCell(sheet, 0, i, style, fieldTitle.get(i));
		}
		// 遍历数据
		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			for (int j = 0; j < fieldName.size(); j++)
			{
				String content = map.get(fieldName.get(j))==null?null:String.valueOf(map.get(fieldName.get(j)));
				ExcelUtil.createCell(sheet, i + 1, j, style, content);
			}
		}
		return workbook;
	}
	
	
	
	
	
	/**
	 * 返回Excel输入流
	 * 
	 * @param fieldName
	 *            列名数组，可以为空
	 * @param data
	 *            数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static InputStream getExcelInputStream(List<String> fieldName, List<Map> data)
	{
		try
		{
			Workbook workbook = ExcelUtil.getHSSFWorkbook(fieldName, data);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			workbook.write(os);
			byte[] content = os.toByteArray();

			return new ByteArrayInputStream(content);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 下载excel
	 * 
	 * @param excelName
	 * @param workbook
	 * @param response
	 */
	public static void responseExcel(String excelName, Workbook workbook, HttpServletResponse response)
	{
		OutputStream os = null;
		String fileName = (excelName.endsWith(".xls") || excelName.endsWith(".xlsx")) ? excelName : excelName + ".xls";
		try
		{
			response.reset(); // 清空输出流
			// response.setContentType("application/msexcel;");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes(), "UTF-8")); // 设定输出文件头
			os = response.getOutputStream(); // 取得输出流
		} catch (IOException ex)
		{// 捕捉异常
			System.out.println("流操作错误:" + ex.getMessage());
		}
		try
		{
			os.flush();
			workbook.write(os);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Output is closed");
		}
	}

	/**
	 * 在输出流中导出excel
	 * 
	 * @param fieldName
	 *            列名数组，可以为空
	 * @param data
	 *            数据
	 * @param excelName
	 *            导出的excel名称 包括扩展名
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	public static void makeStreamExcel(List<String> fieldName, List<Map> data, String excelName, HttpServletResponse response)
	{
		OutputStream os = null;
		try
		{
			response.reset(); // 清空输出流
			// response.setContentType("application/msexcel;");
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes(), "UTF-8")); // 设定输出文件头
			os = response.getOutputStream(); // 取得输出流
		} catch (IOException ex)
		{// 捕捉异常
			System.out.println("流操作错误:" + ex.getMessage());
		}
		// 在内存中生成工作薄
		Workbook workbook = ExcelUtil.getHSSFWorkbook(fieldName, data);
		try
		{
			os.flush();
			workbook.write(os);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Output is closed");
		}
	}

    /**
     * 将Excel2007单个工作薄转换为ListMap
     * 
     * @param file
     * @return
     */
    public static List<Map<String, String>> dataTransferExcel2007(File file)
    {
        return dataTransferExcel2007(file, 0);
    }

    /**
     * 将Excel2007单个工作薄转换为ListMap
     * 
     * @param file
     * @param sheetIndex
     * @return
     */
    public static List<Map<String, String>> dataTransferExcel2007(File file, int sheetIndex)
    {
        return dataTransferExcel2007(file, sheetIndex, 0);
    }

    /**
     * 将Excel2007单个工作薄转换为ListMap
     * 
     * @param file
     * @param sheetIndex
     * @param titleRow
     * @return
     */
    public static List<Map<String, String>> dataTransferExcel2007(File file, int sheetIndex, int titleRow)
    {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            XSSFRow firstRow = sheet.getRow(titleRow);
            XSSFRow contentRow = null;
            int totalRows = sheet.getPhysicalNumberOfRows();
            int totalCols = firstRow.getPhysicalNumberOfCells();
            for (int i = titleRow + 1; i < totalRows; i++)
            {
                contentRow = sheet.getRow(i);
                Map<String, String> map = new HashMap<>();
                boolean emptyRow = true;
                for (int j = contentRow.getFirstCellNum(); j < totalCols; j++)
                {
                    XSSFCell cell = contentRow.getCell(j);
                    String cellValue = getCellvalue(cell).trim();
                    // 只有有一个单元格有内容，此行才为非空行
                    if (cellValue != null && !"".equals(cellValue))
                    {
                        emptyRow = false;
                    }
                    map.put(firstRow.getCell(j).getStringCellValue().trim(), cellValue);
                }
                // 确保为非空行才追加
                if (emptyRow == false)
                {
                    list.add(map);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

	private static String getCellvalue(XSSFCell cell)
	{
		String cellvalue = null;
		if (cell != null)
		{
			switch (cell.getCellType())
			{
			case Cell.CELL_TYPE_NUMERIC:
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell))
				{
					cellvalue = cell.getDateCellValue().toString();
				} else
				{
					Integer num = new Integer((int) cell.getNumericCellValue());
					cellvalue = String.valueOf(num);
				}
				break;
			case Cell.CELL_TYPE_STRING:
				cellvalue = cell.getStringCellValue();
				break;
			default:
				cellvalue = "";
				break;
			}
		} else
		{
			cellvalue = "";
		}
		return cellvalue;
	}
}
