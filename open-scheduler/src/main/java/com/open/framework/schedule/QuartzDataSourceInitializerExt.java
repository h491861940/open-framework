package com.open.framework.schedule;

import com.alibaba.druid.util.JdbcUtils;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSourceInitializer;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: QuartzDataSource 初始化扩展
 * @CreateDate: 2018/7/19 17:45
 * @Version: 1.0
 */
public class QuartzDataSourceInitializerExt extends QuartzDataSourceInitializer {

    private DataSource dataSource;
    /**
     * 当前版本Quartz初始表Names
     */
    private static final List<String> QRTZ_TABLES;

    static {
        QRTZ_TABLES = Arrays.asList("QRTZ_BLOB_TRIGGERS", "QRTZ_CALENDARS", "QRTZ_CRON_TRIGGERS", "QRTZ_FIRED_TRIGGERS", "QRTZ_JOB_DETAILS", "QRTZ_LOCKS", "QRTZ_PAUSED_TRIGGER_GRPS", "QRTZ_SCHEDULER_STATE", "QRTZ_SIMPLE_TRIGGERS", "QRTZ_SIMPROP_TRIGGERS", "QRTZ_TRIGGERS");
    }

    QuartzDataSourceInitializerExt(DataSource dataSource, ResourceLoader resourceLoader, QuartzProperties properties) {
        super(dataSource, resourceLoader, properties);
        this.dataSource = dataSource;
    }


    @Override
    protected DataSourceInitializationMode getMode() {
        //取消配置获取，以数据库中Quartz表是否存在判断是否初始化
        try {
            Connection conn = dataSource.getConnection();
            String dbType = JdbcUtils.getDbType(conn.getMetaData().getURL(), conn.getMetaData().getDriverName());
            if (dbType.equals("h2")) {
                dbType = "mysql";
            }
            List<String> tableNames = JdbcUtils.showTables(conn, dbType).stream().map(String::toUpperCase).collect(Collectors.toList());
            if (tableNames.containsAll(QRTZ_TABLES)) {
                return DataSourceInitializationMode.NEVER;
            } else {
                return DataSourceInitializationMode.ALWAYS;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return DataSourceInitializationMode.ALWAYS;
    }
}
