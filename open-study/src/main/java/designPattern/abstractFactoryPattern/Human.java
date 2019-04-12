package designPattern.abstractFactoryPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 定义一个人类的统称，问题出来了，刚刚定义的时候忘记定义性别了
* 这个重要的问题非修改不可，否则这个世界上太多太多的东西不存在了
*/
public interface Human {
//首先定义什么是人类
 //人是愉快的，会笑的，本来是想用smile表示，想了一下laugh更合适，好长时间没有大笑了；
public void laugh();

//人类还会哭，代表痛苦
public void cry();

//人类会说话
public void talk();

//定义性别
public void sex();
} 