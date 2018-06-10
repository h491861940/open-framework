package com.open.framework.commmon.licence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.open.framework.commmon.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;



public class SystemInfoUtil {
	private static String SysType = "windows";
	private static String licenceInfo;
	public static void main(String[] args) {
		String OS = System.getProperty("os.name").toLowerCase();
		System.out.println(OS);
		System.out.println("------------------------");
		System.out.println("CPU:" + getCpuSn(OS));
		System.out.println("BOARD:" + getMotherboardSn(OS));
		System.out.println("DISK:" + getDiskSn(OS));
		System.out.println("MAC:" + getMacAddressSn(OS));
		System.out.println("------------------------");

		
	}
	public static String getAllInfo(){
		if(StringUtils.isEmpty(licenceInfo)){
			Map map=new HashMap();
			String OS = System.getProperty("os.name").toLowerCase();
	    	String count= "";
	    	String useDate= "";
	    	map.put("cpu",getCpuSn(OS));
	    	map.put("board",getMotherboardSn(OS));
	    	map.put("disk",getDiskSn(OS));
	    	map.put("mac",getMacAddressSn(OS));
	    	map.put("user",count);
	    	map.put("date",useDate);
	    	String info=JsonUtil.toJSONString(map);
	    	licenceInfo=info.substring(1, info.length()-1).replaceAll("\"", "\'");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    	Date now = new Date(); 
    	return licenceInfo+",'nowDate':'"+dateFormat.format(now)+"'";
	}
	/**
	 * 获取CPU信息
	 * windows调用wmi的命令
	 * linux调用本身的系统命令
	 * 
	 * @param type
	 * @return
	 */
	public static String getCpuSn(String type) {
		Process process;
		String result = "";
		try {
			if (type.equals(SysType)) {// 如果是linx系统
				process = Runtime.getRuntime().exec(
						new String[] { "dmidecode", "-t", "processor", "|",
								"grep", "'ID'" });
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					if (line.contains("ID")) {// 获得记录示例   ID: A7 06 02 00 FF FB AB 0F,
						String[] strs = line.split(":");
						if (strs.length > 1) {
							result = strs[1].trim().replace(" ", "");
						}
					}
				}
				input.close();
			} else {// 如果是windows系统
				File file = File.createTempFile("epichust", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new FileWriter(file);
				String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
						+ "Set colItems = objWMIService.ExecQuery _ \n"
						+ "   (\"Select * from Win32_Processor\") \n"
						+ "For Each objItem in colItems \n"
						+ "    Wscript.Echo objItem.ProcessorId \n"//SerialNumber为要获得的数据
						+ "    exit for  ' do the first cpu only! \n"
						+ "Next \n";
				fw.write(vbs);
				fw.close();
				process = Runtime.getRuntime().exec(
						"cscript //NoLogo " + file.getPath());
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("getCpuSn faild");
		}
		return result.trim();
	}

	/**
	 * 获取主板信息
	 * 
	 * @return
	 */
	public static String getMotherboardSn(String type) {
		Process process;
		String result = "";
		try {
			if (type.equals(SysType)) {
				process = Runtime.getRuntime().exec(
						new String[] { "dmidecode", "-s",
								"system-serial-number" });
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				result = result.replace(" ", "");
				input.close();
			} else {
				File file = File.createTempFile("epichust", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new FileWriter(file);
				String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
						+ "Set colItems = objWMIService.ExecQuery _ \n"
						+ "   (\"Select * from Win32_BaseBoard\") \n"
						+ "For Each objItem in colItems \n"
						+ "    Wscript.Echo objItem.SerialNumber \n"//SerialNumber为要获得的数据
						+ "    exit for  ' do the first cpu only! \n"
						+ "Next \n";
				fw.write(vbs);
				fw.close();
				process = Runtime.getRuntime().exec(
						"cscript //NoLogo " + file.getPath());
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("getMotherboard faild");
		}
		return result.trim();
	}

	/**
	 * 获取硬盘信息
	 * 
	 * @return
	 */
	public static String getDiskSn(String type) {
		Process process;
		String result = "";
		try {
			if (type.equals(SysType)) {
				process = Runtime.getRuntime().exec(
						new String[] { "fdisk", "-l" });
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					if (line.contains("Disk identifier")) {// Disk identifier: 0x00023728
						String[] strs = line.split(":");
						if (strs.length > 1) {
							result = strs[1].trim().replace(" ", "");
						}
					}
				}
				input.close();
			} else {
				File file = File.createTempFile("epichust", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new FileWriter(file);
				String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
						+ "Set colItems = objWMIService.ExecQuery _ \n"
						+ "   (\"SELECT * FROM Win32_DiskDrive WHERE (SerialNumber IS NOT NULL) AND (MediaType LIKE 'Fixed hard disk%')\") \n"
						+ "For Each objItem in colItems \n"
						+ "    Wscript.Echo objItem.SerialNumber \n"//SerialNumber为要获得的值
						+ "    exit for  ' do the first cpu only! \n"
						+ "Next \n";
				fw.write(vbs);
				fw.close();
				process = Runtime.getRuntime().exec(
						"cscript //NoLogo " + file.getPath());
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				input.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getDiskSn faild");
		}
		return result.trim();
	}

	/**
	 * 获取MAC地址
	 * 
	 * @return
	 */
	public static String getMacAddressSn(String type) {
		Process process;
		String result = "";
		try {
			if (type.equals(SysType)) {
				process = Runtime.getRuntime().exec(
						new String[] { "ifconfig", "-a" });
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					if (line.contains("HWaddr")) {
						String[] strs = line.split("HWaddr");
						if (strs.length > 1) {
							result = strs[1].trim();
						}
					}
				}
				input.close();
			} else {
				File file = File.createTempFile("epichust", ".vbs");
				file.deleteOnExit();
				FileWriter fw = new FileWriter(file);
				String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
						+ "Set colItems = objWMIService.ExecQuery _ \n"
						+ "   (\"SELECT * FROM Win32_NetworkAdapter WHERE (MACAddress IS NOT NULL) AND (NOT (PNPDeviceID LIKE 'ROOT%'))\") \n"
						+ "For Each objItem in colItems \n"
						+ "    Wscript.Echo objItem.MACAddress \n"//MACAddress为要获得的值
						+ "    exit for  ' do the first cpu only! \n"
						+ "Next \n";
				fw.write(vbs);
				fw.close();
				process = Runtime.getRuntime().exec(
						"cscript //NoLogo " + file.getPath());
				BufferedReader input = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = input.readLine()) != null) {
					result += line;
				}
				input.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getMacAddressSn faild");
		}
		return result.trim();
	}
}