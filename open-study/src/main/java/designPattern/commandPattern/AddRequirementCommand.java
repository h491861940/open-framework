package designPattern.commandPattern;

/**
* @author cbf4Life cbf4life@126.com
* I'm glad to share my knowledge with you all.
* 增加一项需求
*/
public class AddRequirementCommand extends Command {
//执行增加一项需求的命令
public void execute() {
 //找到需求组
 super.rg.find();

 //增加一份需求
 super.rg.add();

 //给出计划
 super.rg.plan();
 }
} 