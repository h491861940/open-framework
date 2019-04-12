public abstract class OneDayTemplate {
    /**
     * 一天的过程
     */
    protected void oneDay(){
        this.getUp();
        this.work();
        this.sleep();
        if(hook()){
            //可以干点啥
        }

    }
    /**
     * 起床
     */
    public abstract void getUp();
    /**
     * 工作
     */
    public abstract void work();
    /**
     * 睡觉
     */
    public abstract void sleep();
    /**
     *  钩子，具体实现可以对算法步骤做一些控制
     *
     * @return
     */
    public boolean hook(){
        return true;
    }
}