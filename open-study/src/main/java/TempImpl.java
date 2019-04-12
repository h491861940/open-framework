
public class TempImpl {

    public String tempCallBackTest(TempCallBack tempCallBack) {
        String str = "开始调用--";
        String returnStr = tempCallBack.doing(str);
        return returnStr;
    }
    public  void getUp(String name){
        System.out.println(name+":起床了");
    }
    public  void sleep(String name){
        System.out.println(name+":睡觉了");
    }
    public String tempCallBackTest(TempCallBack tempCallBack,String name) {
        getUp(name);
        String returnStr = tempCallBack.doing(name);
        System.out.println(returnStr);
        sleep(name);
        return "完成";
    }
}
