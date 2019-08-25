package com.open.framework.commmon.utils;

import com.open.framework.commmon.web.ImportData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

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

	public static Map formatMap=new HashMap();
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
			createCell(sheet, 0, i, style, fieldTitle.get(i));
		}
		// 遍历数据
		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			for (int j = 0; j < fieldName.size(); j++)
			{
				String content = map.get(fieldName.get(j))==null?null:String.valueOf(map.get(fieldName.get(j)));
				createCell(sheet, i + 1, j, style, content);
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
	public static SXSSFWorkbook getSXSSFWorkbook(SXSSFWorkbook workbook, String sheetName, List<String> fieldName, List<String> fieldTitle, List<Map> data)
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
			createCell(sheet, 0, i, style, fieldTitle.get(i));
		}
		// 遍历数据
		for (int i = 0; i < data.size(); i++)
		{
			Map map = data.get(i);
			for (int j = 0; j < fieldName.size(); j++)
			{
				String content = map.get(fieldName.get(j))==null?null:String.valueOf(map.get(fieldName.get(j)));
				createCell(sheet, i + 1, j, style, content);
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

	private static String getCellvalue(Cell cell)
	{
		String cellvalue = null;
		if (cell != null)
		{
			switch (cell.getCellType())
			{
				case NUMERIC:
					if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell))
					{
						cellvalue = cell.getDateCellValue().toString();
					} else
					{
						Integer num = new Integer((int) cell.getNumericCellValue());
						cellvalue = String.valueOf(num);
					}
					break;
				case STRING:
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

	/**
	 * 读EXCEL文件，获取信息集合
	 *
	 * @param
	 * @return
	 */
	public static List<Map<String, Object>> getExcelInfo(MultipartFile mFile, ImportData importData) {
		// 获取文件名
		String fileName = mFile.getOriginalFilename();
		try {
			// 根据文件名判断文件是2003版本还是2007版本
			boolean isExcel2003 = true;
			if (isExcel2007(fileName)) {
				isExcel2003 = false;
			}
			Workbook wb = null;
			// 当excel是2003时,创建excel2003
			if (isExcel2003) {
				wb = new HSSFWorkbook(mFile.getInputStream());
			} else {
				// 当excel是2007时,创建excel2007
				wb = new XSSFWorkbook(mFile.getInputStream());
			}
			return readExcelValue(wb,importData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取Excel里面客户的信息
	 *
	 * @param wb
	 * @return
	 */
	private static  List<Map<String, Object>> readExcelValue(Workbook wb, ImportData importData) {
		// 得到第一个shell
		Sheet sheet = wb.getSheetAt(0);
		// 得到Excel的行数
		int totalRows = sheet.getPhysicalNumberOfRows();
		int totalCells=0;
		List<String>  columns=importData.getColumns();

		// 得到Excel的列数(前提是有行数)
		if (totalRows > 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
		if(CollectionUtils.isEmpty(columns)){
			columns=new ArrayList<>();
			for (int i = 0; i < totalCells; i++) {
				columns.add("默认为column");
			}
		}
		if(totalCells>columns.size()){
			for (int i = 0; i < totalCells-columns.size(); i++) {
				columns.add("默认为column");
			}
		}
		// 循环Excel行数
		for (int r = 1; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			// 循环Excel的列
			Map<String, Object> map = new HashMap<String, Object>();
			for (int c = 0; c < totalCells; c++) {
				Cell cell = row.getCell(c);
				if (null != cell) {
					map.put(columns.get(c),getCellvalue(cell));
				}
			}
			// 添加到list
			infoList.add(map);
		}
		return infoList;
	}

	// @描述：是否是2003的excel，返回true是2003
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	// @描述：是否是2007的excel，返回true是2007
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
}
