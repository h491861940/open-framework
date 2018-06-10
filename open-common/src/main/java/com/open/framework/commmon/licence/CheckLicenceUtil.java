package com.open.framework.commmon.licence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CheckLicenceUtil
{

	public static void main(String[] args) throws Exception
	{
		//第一个是cpu,第二个是主板，第三个是硬盘
		//BFEBFBFF000206A7=WB01683812=WD-WX41EA1SUC24 可以用于生成licence，通过"="来截取，也存在=WB01683812=WD-WX41EA1SUC24
		//或者==WD-WX41EA1SUC24,=WB01683812= 这几种，说明存在空，所以固定按"="截取
	    //String key= createLicence("no","2018-06-02");
        //RSAUtils.createUserKey(key);
        byte[] userKey =RSAUtils.getUserKey();
        byte[] decodedData = RSAUtils.decryptByPrivateKey(userKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
        boolean tf= checkLicence(target);
        System.out.println("验证结果"+tf);
        if(!tf){
            System.out.println("关闭");
           // System.exit(0);
        }
	}
	public CheckLicenceUtil(){
		try{
			byte[] userKey =RSAUtils.getUserKey();
			byte[] decodedData = RSAUtils.decryptByPrivateKey(userKey);
			String target = new String(decodedData);
			boolean tf= checkLicence(target);
			System.out.println("验证结果"+tf);
			if(!tf){
				System.exit(0);
			}
		}catch (Exception e){
			System.exit(0);
		}
	}
	public static boolean checkLicence(String code) throws ParseException{
	    JSONObject  jsonObject = JSONObject.parseObject(code);
	    //json对象转Map
	    Map<String,Object> map = (Map<String,Object>)jsonObject;
		String OS = System.getProperty("os.name").toLowerCase();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		if(System.currentTimeMillis()>(df.parse((String)map.get("date"))).getTime())
		{
			return false;
		}
		if(null!=map.get("cpu")){
			if(!map.get("cpu").equals(SystemInfoUtil.getCpuSn(OS))){
				return false;
			}
			if(!map.get("board").equals(SystemInfoUtil.getMotherboardSn(OS))){
				return false;
			}
			if(!map.get("disk").equals(SystemInfoUtil.getDiskSn(OS))){
				return false;
			}
			if(!map.get("mac").equals(SystemInfoUtil.getMacAddressSn(OS))){
				return false;
			}
		}
		return true;
	}
	public static String createLicence(String isTry,String date){
		Map map=new HashMap();
		map.put("date", date);
		if("yes".equals(isTry)){//是试用版，只写入时间和用户数
			map.put("user", "5");
		}else{//不是试用版，机器码也写入
			map.put("user", "2000");
			String OS = System.getProperty("os.name").toLowerCase();
			map.put("cpu", SystemInfoUtil.getCpuSn(OS));
			map.put("board", SystemInfoUtil.getMotherboardSn(OS));
			map.put("disk", SystemInfoUtil.getDiskSn(OS));
			//{"DISK":"WD-WX41EA1SUC24","BOARD":"WB01683812","CPU":"BFEBFBFF000206A7"}
			map.put("mac", SystemInfoUtil.getMacAddressSn(OS));
		}
		return JSON.toJSONString(map);
	}
}
