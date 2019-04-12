package designPattern.abstractFactoryPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 世界上有哪些类型的人，列出来
* JDK 1.5开始引入enum类型也是目的的，吸引C程序员转过来
*/
public enum HumanEnum {
//把世界上所有人类型都定义出来
YelloMaleHuman("designPattern.abstractFactoryPattern.YellowMaleHuman"),

YelloFemaleHuman("designPattern.abstractFactoryPattern.YellowFemaleHuman"),

WhiteFemaleHuman("designPattern.abstractFactoryPattern.WhiteFemaleHuman"),

WhiteMaleHuman("designPattern.abstractFactoryPattern.WhiteMaleHuman"),

BlackFemaleHuman("designPattern.abstractFactoryPattern.BlackFemaleHuman"),

BlackMaleHuman("designPattern.abstractFactoryPattern.BlackMaleHuman");
 

private String value = "";
//定义构造函数，目的是Data(value)类型的相匹配
private HumanEnum(String value){
 this.value = value;
 }

public String getValue(){
 return this.value;
 }


/*
 * java enum类型尽量简单使用，尽量不要使用多态、继承等方法
 * 毕竟用Clas完全可以代替enum
 */


}