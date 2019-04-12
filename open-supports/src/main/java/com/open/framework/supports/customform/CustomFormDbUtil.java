package com.open.framework.supports.customform;

import com.open.framework.commmon.utils.JsonUtil;
import com.open.framework.commmon.utils.SpringUtil;
import com.open.framework.commmon.utils.StringUtil;
import com.open.framework.supports.customform.entity.CustomForm;
import com.open.framework.supports.customform.entity.CustomFormChild;
import com.open.framework.supports.customform.entity.CustomFormDetail;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: hsj
 * @Date: 2019/3/25 13:36
 * @Description: 自定义表单数据库操作接口
 */
public class CustomFormDbUtil {
    private static JdbcTemplate jdbcTemplate;

    private static JdbcTemplate getJdbcTemplate() {
        jdbcTemplate= SpringUtil.getBean(JdbcTemplate.class);
        /*DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.138.67:3306/demo?characterEncoding=UTF-8&autoReconnect=true&useSSL" +
                "=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        jdbcTemplate = new JdbcTemplate(dataSource);*/
        return jdbcTemplate;
    }

    public static boolean getAllTableName(String tableName) throws Exception {
        Connection conn = getJdbcTemplate().getDataSource().getConnection();
        ResultSet tabs = null;
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            String[] types = {"TABLE"};
            tabs = dbMetaData.getTables(null, null, tableName, types);
            if (tabs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tabs.close();
            conn.close();
        }
        return false;
    }

    public static List getFieldType() {
        List list = new ArrayList();
        list.add(new HashMap<String, String>() {{
            put("key", "String");
            put("value", "文本");
        }});
        list.add(new HashMap<String, String>() {{
            put("key", "Integer");
            put("value", "数字");
        }});
        list.add(new HashMap<String, String>() {{
            put("key", "textarea");
            put("value", "文本域");
        }});
        list.add(new HashMap<String, String>() {{
            put("key", "date");
            put("value", "日期");
        }});
        return list;
    }

    private static String getDbFieldType(String type) {
        String dbType = "mysql";
        if (StringUtil.isEmpty(type) || "String".equals(type)) {
            return dbType.equals("mysql") ? " varchar(100) " : " varchar2(100)  ";
        } else {
            if ("Integer".equals(type)) {
                return dbType.equals("mysql") ? " int " : " number ";
            } else if ("textarea".equals(type)) {
                return dbType.equals("mysql") ? " varchar(1000) " : " varchar2(1000) ";
            } else if ("date".equals(type)) {
                return dbType.equals("mysql") ? " timestamp " : " date ";
            }
        }
        return " varchar(100) ";
    }

    public static CustomForm generateTable(CustomForm customForm) throws Exception {
        List sqlList = new ArrayList();
        String tableName = StringUtil.camelToUnderline(customForm.getCode());
        customForm.setTableName(tableName);
        if (getAllTableName(tableName)) {
            sqlList.addAll(getTableUpdateSql(tableName, customForm.getFields()));
        } else {
            if (CollectionUtils.isNotEmpty(customForm.getFields())) {
                sqlList.add(getCreateTableSql(tableName, customForm.getFields(), null));
            }
        }

        List<CustomFormChild> customFormChildList = customForm.getChilds();
        for (int i = 0; i < customFormChildList.size(); i++) {
            CustomFormChild customFormChild = customFormChildList.get(i);
            String childTableName = StringUtil.camelToUnderline(customFormChild.getCode());
            customFormChild.setTableName(childTableName);
            if (getAllTableName(childTableName)) {
                sqlList.addAll(getTableUpdateSql(childTableName, customFormChild.getFields()));
            } else {
                if (CollectionUtils.isNotEmpty(customFormChild.getFields())) {
                    sqlList.add(getCreateTableSql(childTableName, customFormChild.getFields(), tableName));
                }
            }
        }
        String[] strings = new String[sqlList.size()];
        sqlList.toArray(strings);
        getJdbcTemplate().batchUpdate(strings);
        return customForm;
    }

    private static List getTableUpdateSql(String tableName, List<CustomFormDetail> customFormDetailList) {
        List<String> list = new ArrayList();
        if (CollectionUtils.isEmpty(customFormDetailList)) {
            return list;
        }
        for (int i = 0; i < customFormDetailList.size(); i++) {
            CustomFormDetail customFormDetail = customFormDetailList.get(i);
            String fieldName = StringUtil.camelToUnderline(customFormDetail.getCode());
            String fieldType = getDbFieldType(customFormDetail.getFieldType());
            if ( 1 == customFormDetail.getCudState()) {
                //新增
                list.add("ALTER TABLE `" + tableName + "` add `" + fieldName + "` " + fieldType + " ;");
            } else if ( 2 == customFormDetail.getCudState()) {
                //修改
                list.add("ALTER TABLE `" + tableName + "` modify `" + fieldName + "` " + fieldType + " ;");
            } else if (3 == customFormDetail.getCudState()) {
                //删除
                list.add("ALTER TABLE `" + tableName + "` DROP `" + fieldName + "` ;");
            }
        }
        return list;
    }

    private static String getCreateTableSql(String tableName, List<CustomFormDetail> list, String parentTableName) {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE `" + tableName + "` (");
        sb.append(" `gid` varchar(32) NOT NULL ,");
        if (StringUtil.isNotEmpty(parentTableName)) {
            sb.append(" `" + parentTableName + "_gid` varchar(32) ,");
        }
        for (CustomFormDetail customFormDetail : list) {
            String fieldName = StringUtil.camelToUnderline(customFormDetail.getCode());
            String fieldType = getDbFieldType(customFormDetail.getFieldType());
            sb.append("`" + fieldName + "` " + fieldType + " ,");
        }
        sb.append(" PRIMARY KEY (`gid`)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        CustomForm customForm = new CustomForm();
        customForm.setCode("MrlDemo");
        customForm.setName("测试");
        customForm.setApplicationGid("qqq");
        customForm.setJsonData("页面数据");
        //customForm.setState(0);

        List<CustomFormDetail> customFormDetailList = new ArrayList();
        customFormDetailList.add(new CustomFormDetail().setCode("mrlCode").setName("物料编码").setFieldType("String").setSeq(1));
        customFormDetailList.add(new CustomFormDetail().setCode("mrlName").setName("物料名称").setFieldType("String").setSeq(2));
        customFormDetailList.add(new CustomFormDetail().setCode("mrlType").setName("物料类型").setFieldType("Integer").setSeq(3));
        //customFormDetailList.add(new CustomFormDetail().setCode("mrlTypeAsd").setName("物料类型2").setType("Integer").setSeq(4).setCudState(1));

        customForm.setFields(customFormDetailList);
        List<CustomFormChild> customFormChildList = new ArrayList();

        List<CustomFormDetail> customFormDetailList1 = new ArrayList();
        customFormDetailList1.add(new CustomFormDetail().setCode("productCode").setName("产品编码").setFieldType("String").setSeq(1));
        customFormDetailList1.add(new CustomFormDetail().setCode("productName").setName("产品名称").setFieldType("String").setSeq(2));
        customFormChildList.add(new CustomFormChild().setCode("productDemo").setName("产品").setFields(customFormDetailList1));

        List<CustomFormDetail> customFormDetailList2 = new ArrayList();
        customFormDetailList2.add(new CustomFormDetail().setCode("supplierCode").setName("供应商编码").setFieldType("String").setSeq(1));
        customFormDetailList2.add(new CustomFormDetail().setCode("supplierName").setName("供应商名称").setFieldType("String").setSeq(2));
        customFormChildList.add(new CustomFormChild().setCode("suppliertDemo").setName("供应商").setFields(customFormDetailList2));
        customForm.setChilds(customFormChildList);
        //getJdbcTemplate().execute("create table temp(id int primary key,name varchar(32))");
        //generateTable(customForm);
        System.out.println( JsonUtil.toJSONString(customForm));
    }

    public static void insert() {

    }
}
