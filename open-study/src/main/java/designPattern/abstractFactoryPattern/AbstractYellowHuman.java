package designPattern.abstractFactoryPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 为什么要修改成抽象类呢？要定义性别呀
*/
public abstract class AbstractYellowHuman implements Human {

public void cry() {
 System.out.println("黄色人种会哭");
 } 

public void laugh() {
 System.out.println("黄色人种会大笑，幸福呀！");
 }
public void talk() {
 System.out.println("黄色人种会说话，一般说的都是双字节");
 }

}