
public class Tom extends OneDayTemplate {

    @Override
    public void getUp() {
        System.out.println("tom起床了");
    }

    @Override
    public void work() {
        System.out.println("打开电脑开始打游戏");
    }

    @Override
    public void sleep() {
        System.out.println("玩了一天游戏睡觉了");
    }

}