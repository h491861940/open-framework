package designPattern.bridgePattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 这是我整个集团公司的产品类
*/
public abstract class Product {

//甭管是什么产品它总要是能被生产出来
public abstract void beProducted(); 

//生产出来的东西，一定要销售出去，否则扩本呀
public abstract void beSelled();

} 