package com.flying.sql.query;

import com.baomidou.mybatisplus.generator.config.querys.SqlServerQuery;

/**
 * @author Administrator
 * @date 2019/4/10
 */
public class ISqlServerQuery extends SqlServerQuery {
    @Override
    public String tableFieldsSql() {
        return "SELECT  cast(a.NAME AS VARCHAR(500)) AS TABLE_NAME,cast(b.NAME AS VARCHAR(500)) AS COLUMN_NAME, "
                + "cast(c.VALUE AS VARCHAR(500)) AS COMMENTS,cast(sys.types.NAME AS VARCHAR (500)) AS DATA_TYPE,"
                + "(" + " SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END"
                + " FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes "
                +
                " WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (A.NAME) AND sysobjects.xtype = 'PK'"
                + " AND sysobjects.parent_obj = syscolumns.id " + " AND sysindexes.id = syscolumns.id "
                + " AND sysobjects.NAME = sysindexes.NAME AND sysindexkeys.id = syscolumns.id "
                + " AND sysindexkeys.indid = sysindexes.indid "
                + " AND syscolumns.colid = sysindexkeys.colid AND syscolumns.NAME = B.NAME) as 'KEY',"
                + "b.is_identity isIdentity,"
                + "b.is_nullable as isNullable,"
                + "CASE WHEN CHARINDEX('char', cast(sys.types.NAME AS VARCHAR(500))) > 0 "
                + "THEN cast(b.max_length / 2 AS VARCHAR(10)) ELSE '' END as maxLength"
                + " FROM ( select name,object_id from sys.tables UNION all select name,object_id from sys.views ) a "
                + " INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id "
                + " LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id   "
                + " LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id "
                + " WHERE a.NAME = '%s' and sys.types.NAME !='sysname' ";
    }

    @Override
    public String[] fieldCustom() {
        return new String[]{"isNullable", "maxLength"};
    }
}
