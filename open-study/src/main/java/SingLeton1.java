/**
 * @Auther: hsj
 * @Date: 2019/7/11 22:34
 * @Description:
 */
public enum SingLeton1 {
    INSTAn(2),
    INAAA(1);
    private int i;
    SingLeton1(int i) {
        this.i=i;
    }

    public  void test(){
        System.out.println(i+"2222");
    }

}
