package com.flying.utils;


import org.apache.commons.lang3.StringUtils;

import java.sql.JDBCType;

/**
 * @author Administrator
 * @date 2019/5/30
 * 获取SqlServer数据库字段类型对应的JDBC类型
 */
public class SqlServerJdbcTypeConvert implements IJdbcTypeConvert {
    @Override
    public JDBCType convert(String columnType) {
        if (StringUtils.isBlank(columnType)) {
            return null;
        }
        String t = columnType.toLowerCase();
        if (t.contains("nchar")) {
            return JDBCType.NCHAR;
        } else if (t.contains("nvarchar")) {
            return JDBCType.NVARCHAR;
        } else if (t.contains("ntext")) {
            return JDBCType.LONGNVARCHAR;
        } else if (t.contains("varchar")) {
            return JDBCType.VARCHAR;
        } else if (t.contains("char")) {
            return JDBCType.CHAR;
        } else if (t.contains("text")) {
            return JDBCType.LONGVARCHAR;
        } else if (t.contains("bit")) {
            return JDBCType.BIT;
        } else if (t.contains("varbinary")) {
            return JDBCType.VARBINARY;
        } else if (t.contains("binary")) {
            return JDBCType.BINARY;
        } else if (t.contains("image")) {
            return JDBCType.LONGVARBINARY;
        } else if (t.contains("tinyint")) {
            return JDBCType.TINYINT;
        } else if (t.contains("smallint")) {
            return JDBCType.SMALLINT;
        } else if (t.contains("bigint")) {
            return JDBCType.BIGINT;
        } else if (t.contains("int")) {
            return JDBCType.INTEGER;
        } else if (t.contains("decimal") || t.contains("money")) {
            return JDBCType.DECIMAL;
        } else if (t.contains("numeric")) {
            return JDBCType.NUMERIC;
        } else if (t.contains("float")) {
            return JDBCType.FLOAT;
        } else if (t.contains("real")) {
            return JDBCType.REAL;
        } else if (t.contains("datetime") || t.contains("timestamp")) {
            return JDBCType.TIMESTAMP;
        } else if (t.contains("date")) {
            return JDBCType.DATE;
        } else if (t.contains("time")) {
            return JDBCType.TIME;
        } else if (t.contains("xml")) {
            return JDBCType.SQLXML;
        } else if (t.contains("cursor")) {
            return JDBCType.REF_CURSOR;
        }
        return JDBCType.NVARCHAR;
    }
}
