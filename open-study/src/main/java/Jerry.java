
public class Jerry extends OneDayTemplate {

    @Override
    public void getUp() {
        System.out.println("jerry起床洗脸了");
    }

    @Override
    public void work() {
        System.out.println("去上班,然后逛街,然后回家");
    }

    @Override
    public void sleep() {
        System.out.println("洗脸睡觉");
    }

}