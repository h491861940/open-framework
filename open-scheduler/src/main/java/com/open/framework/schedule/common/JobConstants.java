package com.open.framework.schedule.common;

/**
 * 常量接口
 *
 * @date :2018-07-20
 */
public interface JobConstants {

    /**
     * The constant KEY_NAME_PREFIX.
     */
    String KEY_NAME_PREFIX = "job_";

    /**
     * The constant KEY_GROUP_PREFIX.
     */
    String KEY_GROUP_PREFIX = "group_";

    /**
     * The constant KEY_NAME_TPL.
     */
    String KEY_NAME_TPL = KEY_NAME_PREFIX + "%s";

    /**
     * The constant KEY_GROUP_TPL.
     */
    String KEY_GROUP_TPL = KEY_GROUP_PREFIX + "%s";

    /**
     * 每次执行
     */
    String EACH_TIME = "0";

    /**
     * 间隔时间执行
     */
    String INTERVAL_TIME = "1";

    /**
     * 指定时间执行
     */
    String APPOINT_TIME = "2";



    /**
     * 解析job key name中对应的Task ID
     *
     * @param jobKeyName
     * @return
     */
    static String parseTaskId(String jobKeyName) {
        if (jobKeyName == null || jobKeyName.length() < 1) {
            return null;
        }
        if (jobKeyName.startsWith(KEY_NAME_PREFIX)) {
            return jobKeyName.substring(KEY_NAME_PREFIX.length());
        }
        return null;
    }

    /**
     * 解析job key group中对应的Task分类ID
     *
     * @param jobKeyGroup
     * @return
     */
    static String parseTaskCategoryId(String jobKeyGroup) {
        if (jobKeyGroup == null || jobKeyGroup.length() < 1) {
            return null;
        }
        if (jobKeyGroup.startsWith(KEY_GROUP_PREFIX)) {
            return jobKeyGroup.substring(KEY_GROUP_PREFIX.length());
        }
        return null;
    }

}
