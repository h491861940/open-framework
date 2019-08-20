/**
 * @Auther: hsj
 * @Date: 2019/7/11 22:30
 * @Description:
 */
public class Singleton {
    private Singleton(){

    }
    private static class LazyClass{
        private static final  Singleton singleton=new Singleton();
    }
    public static final Singleton getI(){
        return LazyClass.singleton;
    }
}
