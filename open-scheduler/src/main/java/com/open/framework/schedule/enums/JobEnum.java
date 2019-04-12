package com.open.framework.schedule.enums;


import com.open.framework.commmon.BaseConstant;
import com.open.framework.commmon.enums.IEnumFace;
import com.open.framework.commmon.enums.NVPair;

import java.util.Optional;
import java.util.stream.Stream;

public final class JobEnum {

    public enum TaskCode implements IEnumFace {
        addJobException(2000, "新增定时任务异常:{0}"),
        modifyJobException(2001, "修改定时任务异常:{0}"),
        deleteJobException(2002, "删除定时任务异常:{0}"),
        startUpJobException(2003, "启动定时任务异常:{0}"),
        shutDownJobException(2004, "关闭定时任务异常:{0}"),
        pauseJobException(2005, "暂停定时任务异常:{0}"),
        resumeJobException(2006, "恢复定时任务异常:{0}"),
        executeNowJobException(2007, "立即执行定时任务异常:{0}"),
        operTaskParamException(2008, "没有权限操作系统级别参数"),
        noUserCodeException(2009, "没有用户信息"),
        changeStateException(2010, "只能操作暂停和恢复"),
        atonceException(2011, "立即执行失败:{0}"),
        updateCronException(2012, "更新cron表达式");

        private NVPair p;

        TaskCode(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }

    public enum Level implements IEnumFace {
        system(0, "系统级"),

        user(3, "用户级");

        private NVPair p;

        Level(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }
    public enum TaskType implements IEnumFace{

        /**
         * 延时任务类型
         */
        SINGLE(0, "单次执行"),

        /**
         * 定时任务类型
         */
        CYCLE(1, "循环执行");
        private NVPair p;

        TaskType(Integer val, String description) {
            this.p = new NVPair(val, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public Integer getVal() {
            return p.getVal();
        }

        public String getText() {
            return p.getText();
        }
    }
    public enum CurrentState implements IEnumFace{
        /**
         * 等待
         */
        WAITING("0", "等待"),
        /**
         * 执行中
         */
        EXECUTING("1", "执行中"),

        /**
         * 任务执行异常
         */
        EXCEPTION("2", "任务结果异常"),
        /**
         * 暂停
         */
        PAUSED("3", "暂停"),
        /**
         * 任务执行失败
         */
        FAILED("4", "任务失败");
        private NVPair p;

        CurrentState(String code, String description) {
            this.p = new NVPair(code, description);
        }

        @Override
        public NVPair getPair() {
            return p;
        }

        public String getCode() {
            return p.getCode();
        }
    }
}
