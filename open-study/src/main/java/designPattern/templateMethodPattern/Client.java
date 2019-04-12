package designPattern.templateMethodPattern;

/**
 * @author cbf4Life cbf4life@126.com
 * I'm glad to share my knowledge with you all.
 * 客户开始使用这个模型
 */
public class Client {
    public static void main(String[] args) {
        //客户开着H1型号，出去遛弯了
        HummerModel h1 = new HummerH1Model();
        h1.run(); //汽车跑起来了；

        //客户开H2型号，出去玩耍了
        HummerModel h2 = new HummerH2Model();
        h2.run();

        HummerH1Model h11 = new HummerH1Model();
        h11.setAlarm(true);
        h11.run(); //汽车跑起来了
    }
} 