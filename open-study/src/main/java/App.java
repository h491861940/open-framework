public class App {
    public static void main(String[] args) {
        Tom tom = new Tom();
        tom.oneDay();
        
        System.out.println("-----------------------------");
        
        Jerry jerry = new Jerry();
        jerry.oneDay();
        TempImpl tempImpl=new TempImpl();
        String info=tempImpl.tempCallBackTest(new TempCallBack(){
            @Override
            public String doing(String str) {
                return str+"我自己的逻辑啊";
            }
        });
        System.out.println(info);

        TempImpl tempImpl2=new TempImpl();
        String info2=tempImpl2.tempCallBackTest(str -> str+"开始干活了","hsj");
        System.out.println(info2);
    }
}