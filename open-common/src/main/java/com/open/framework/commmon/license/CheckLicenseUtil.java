package com.open.framework.commmon.license;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.RSAUtils;
import org.apache.commons.lang3.StringUtils;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CheckLicenseUtil
{
	private static String licenseInfo;
	public static Integer userCount=10;
	public static void main(String[] args) throws Exception
	{

		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		HWDiskStore[] diskStores=hal.getDiskStores();
		NetworkIF[] networkIFs=hal.getNetworkIFs();
		LicenseDTO licenseDto=new LicenseDTO();
		licenseDto.setExpireDate("2019-06-02");
		licenseDto.setTempTry("no");
		licenseDto.setUserCount(2000);
		Baseboard baseboard = hal.getComputerSystem().getBaseboard();
		licenseDto.setCpu(hal.getProcessor().getProcessorID().trim());
		licenseDto.setBoard(baseboard.getSerialNumber());
		if(null!=diskStores && diskStores.length>0){
			licenseDto.setDisk(diskStores[0].getSerial().trim().trim());
		}
		if(null!=networkIFs && networkIFs.length>0){
			licenseDto.setMac(networkIFs[0].getMacaddr().trim());
		}
		String key= createLicense(licenseDto);
		RSAUtils.createUserKey(key);
		byte[] userKey = RSAUtils.getUserKey();
		byte[] decodedData = RSAUtils.decryptByPrivateKey(userKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
		boolean tf= checkLicense(target);
		System.out.println("验证结果"+tf);
		if(!tf){
			System.out.println("关闭");
			// System.exit(0);
		}
	}
	public CheckLicenseUtil(){
		try{
			byte[] userKey = RSAUtils.getUserKey();
			byte[] decodedData = RSAUtils.decryptByPrivateKey(userKey);
			String target = new String(decodedData,"UTF-8");
			boolean tf= checkLicense(target);
			System.out.println("验证结果"+tf);
			if(!tf){
				System.exit(0);
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	public static String getAllInfo(){
		if(StringUtils.isEmpty(licenseInfo)){
			SystemInfo si = new SystemInfo();
			HardwareAbstractionLayer hal = si.getHardware();
			Map map=new HashMap();
			map.put("cpu",hal.getProcessor().getProcessorID().trim());
			Baseboard baseboard = hal.getComputerSystem().getBaseboard();
			map.put("board",baseboard.getSerialNumber());
			HWDiskStore[] diskStores=hal.getDiskStores();
			if(null!=diskStores && diskStores.length>0){
				map.put("disk",diskStores[0].getSerial().trim());
			}
			NetworkIF[] networkIFs=hal.getNetworkIFs();
			if(null!=networkIFs && networkIFs.length>0){
				map.put("mac",networkIFs[0].getMacaddr().trim());
			}
			String info=JsonUtil.toJSONString(map);
			licenseInfo=info;
		}
		return licenseInfo;
	}
	/**
	 * 检验license
	 * @param code
	 * @return
	 * @throws ParseException
	 */
	public static boolean checkLicense(String code) throws ParseException{
		JSONObject jsonObject = JSONObject.parseObject(code);
		//json对象转Map
		Map<String,Object> map = (Map<String,Object>)jsonObject;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		if(System.currentTimeMillis()>(df.parse((String)map.get("date"))).getTime())
		{
			return false;
		}
		if(null!=map.get("tempTry") && !"yes".equals(map.get("tempTry")+"")){
			SystemInfo si = new SystemInfo();
			HardwareAbstractionLayer hal = si.getHardware();
			Baseboard baseboard = hal.getComputerSystem().getBaseboard();
			if(!map.get("cpu").equals(hal.getProcessor().getProcessorID().trim())){
				return false;
			}
			if(!map.get("board").equals(baseboard.getSerialNumber().trim())){
				return false;
			}
			HWDiskStore[] diskStores=hal.getDiskStores();
			if(null!=diskStores && !Arrays.stream(diskStores).map(t-> t.getSerial().trim()).collect(Collectors.toList()).contains(map.get("disk"))){
				return false;
			}
			NetworkIF[] networkIFs=hal.getNetworkIFs();
			if(null!=diskStores && !Arrays.stream(networkIFs).map(t-> t.getMacaddr().trim()).collect(Collectors.toList()).contains(map.get("mac"))){
				return false;
			}
		}

		if(null!=map.get("userCount")){
			userCount=Integer.parseInt(map.get("userCount")+"");
		}

		return true;
	}

	/**
	 * 组建license
	 * @param licenseDTO
	 * @return
	 */
	public static String createLicense(LicenseDTO licenseDTO){
		Map map=new HashMap();
		map.put("date", licenseDTO.getExpireDate());
		//是试用版，只写入时间和用户数
		if("yes".equals(licenseDTO.getTempTry())){
			//map.put("userCount", licenseDto.getUserCount());//默认10个
		}else{//不是试用版，机器码也写入
			map.put("userCount", licenseDTO.getUserCount());
			map.put("cpu", licenseDTO.getCpu());
			map.put("board", licenseDTO.getBoard());
			map.put("disk", licenseDTO.getDisk());
			map.put("mac", licenseDTO.getMac());
		}
		map.put("tempTry", licenseDTO.getTempTry());
		return JSON.toJSONString(map);
	}

	/**
	 * 生成加密文件,用于系统启动时候解密
	 * @param licenseDTO
	 * @return
	 * @throws Exception
	 */
	public static byte[] generate(LicenseDTO licenseDTO) throws Exception {
		String key=createLicense(licenseDTO);
		return RSAUtils.generateData(key);
	}
}
