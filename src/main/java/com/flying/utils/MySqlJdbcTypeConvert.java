package com.flying.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.JDBCType;

/**
 * @author Administrator
 * @date 2019/5/28
 * 获取Mysql数据库字段类型对应的JDBC类型
 */
public class MySqlJdbcTypeConvert implements IJdbcTypeConvert {
    @Override
    public JDBCType convert(String columnType) {
        if (StringUtils.isBlank(columnType)) {
            return null;
        }
        String t = columnType.toLowerCase();
        if (t.contains("varchar")) {
            return JDBCType.VARCHAR;
        } else if (t.contains("char")) {
            return JDBCType.CHAR;
        } else if (t.contains("text")) {
            return JDBCType.LONGVARCHAR;
        } else if (t.contains("bigint")) {
            return JDBCType.BIGINT;
        } else if (t.contains("tinyint(1)")) {
            return JDBCType.TINYINT;
        } else if (t.contains("smallint")) {
            return JDBCType.SMALLINT;
        } else if (t.contains("int")) {
            return JDBCType.INTEGER;
        } else if (t.contains("bit")) {
            return JDBCType.BIT;
        } else if (t.contains("decimal")) {
            return JDBCType.DECIMAL;
        } else if (t.contains("clob")) {
            return JDBCType.CLOB;
        } else if (t.contains("blob")) {
            return JDBCType.BLOB;
        } else if (t.contains("float")) {
            return JDBCType.FLOAT;
        } else if (t.contains("double")) {
            return JDBCType.DOUBLE;
        } else if (t.contains("datetime") || t.contains("timestamp")) {
            return JDBCType.TIMESTAMP;
        } else if (t.contains("date") || t.contains("year")) {
            return JDBCType.DATE;
        } else if (t.contains("time")) {
            return JDBCType.TIME;
        }
        return JDBCType.VARCHAR;
    }
}
