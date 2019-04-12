package designPattern.abstractFactoryPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.\
* 女性创建工厂
*/
public class FemaleHumanFactory extends AbstractHumanFactory {
//创建一个女性黑种人
public Human createBlackHuman() {
 return super.createHuman(HumanEnum.BlackFemaleHuman);
 }
//创建一个女性白种人
public Human createWhiteHuman() {
 return super.createHuman(HumanEnum.WhiteFemaleHuman);
 }
//创建一个女性黄种人
public Human createYellowHuman() {
 return super.createHuman(HumanEnum.YelloFemaleHuman);
 }
} 