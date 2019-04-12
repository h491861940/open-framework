package designPattern.bridgePattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 生产iPod了
*/
public class IPod extends Product {
public void beProducted() {
 System.out.println("生产出的iPod是这个样子的...");
 }

public void beSelled() {
 System.out.println("生产出的iPod卖出去了...");
 }
} 