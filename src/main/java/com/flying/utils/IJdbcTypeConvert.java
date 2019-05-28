package com.flying.utils;

import java.sql.JDBCType;

/**
 * @author Administrator
 * @date 2019/5/28
 */
public interface IJdbcTypeConvert {

    /**
     * 获取数据库字段类型对应的JDBC类型
     *
     * @param columnType 数据库字段类型
     * @return JDBC类型
     */
    public JDBCType convert(String columnType);
}
