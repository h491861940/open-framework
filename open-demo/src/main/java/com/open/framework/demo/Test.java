package com.open.framework.demo;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static List<String> getTableNameBySql(String sql) throws JSQLParserException {
        CCJSqlParserManager parser=new CCJSqlParserManager();
        List<String> list=new ArrayList<String>();
        Statement stmt = CCJSqlParserUtil.parse(sql);

        if (stmt instanceof Select) {
            Select selectStatement = (Select) stmt;
            SelectBody selectBody=selectStatement.getSelectBody();
            PlainSelect plainSelect= (PlainSelect)selectBody;
            Expression aa= plainSelect.getWhere();
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List tableList = tablesNamesFinder.getTableList(selectStatement);

            for (Iterator iter = tableList.iterator(); iter.hasNext();) {
                String tableName=iter.next().toString();
                list.add(tableName);
            }
        }
        return list;
    }

    public static void main(String[] args) throws JSQLParserException{
       System.out.println(getTableNameBySql("select gid,(select * from teacher where name=a.name) from student a, classroom b left join schoole s on s.gid=b.aid where 1=1 and a.git=b.cid "));
    }
}
