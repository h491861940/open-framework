package designPattern.abstractFactoryPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 白色人人种
* 为了代码整洁，新建一个包，这里是白种人的天下了
*/
public abstract class AbstractWhiteHuman implements Human {
public void cry() {
 System.out.println("白色人种会哭");
 }
public void laugh() {
 System.out.println("白色人种会大笑，侵略的笑声");
 } 

public void talk() {
 System.out.println("白色人种会说话，一般都是但是单字节！");
 }
} 