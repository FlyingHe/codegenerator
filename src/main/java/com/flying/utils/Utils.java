package com.flying.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class Utils {
    public static String getPkgFromUnderline(String tableName) {
        return tableName.replace("_", ".").toLowerCase();
    }

    public static String getPkgFromCamelCase(String tableName) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(tableName), ".").toLowerCase();
    }

    public static String getPkg(String tableName) {
        return tableName.contains("_") ? getPkgFromUnderline(tableName) : getPkgFromCamelCase(tableName);
    }

    public static String getSourcePath(String tableName) {
        return getPkg(tableName).replace(".", File.separator);
    }

    public static String makeFirstLetterLowerCaseOf(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static void handleImportPkgs(TableInfo tableInfo) {
        //移除Mybatis自己的注解
        List<String> excludePkg = new ArrayList<>();
        tableInfo.getImportPackages().forEach(pkg -> {
            if (pkg.startsWith("com.baomidou.mybatisplus.annotation")) {
                excludePkg.add(pkg);
            }
        });
        excludePkg.forEach(pkg -> tableInfo.getImportPackages().remove(pkg));
        //根据字段校验情况添加校验注解
        boolean hasSize = false;
        boolean hasNotBlank = false;
        boolean hasNotNull = false;
        for (TableField tableField : tableInfo.getFields()) {
            if (!hasSize) {
                hasSize = shouldAddSizeAnno(tableField);
            }
            if (!hasNotBlank) {
                hasNotBlank = shouldAddNotBlankAnno(tableField);
            }
            if (!hasNotNull) {
                hasNotNull = shouldAddNotNullAnno(tableField);
            }
            if (hasSize && hasNotBlank && hasNotNull) {
                break;
            }
        }
        if (hasSize) {
            tableInfo.getImportPackages().add("javax.validation.constraints.Size");
        }
        if (hasNotBlank) {
            tableInfo.getImportPackages().add("javax.validation.constraints.NotBlank");
        }
        if (hasNotNull) {
            tableInfo.getImportPackages().add("javax.validation.constraints.NotNull");
        }
    }

    public static boolean shouldAddSizeAnno(TableField tableField) {
        return StringUtils.isNotBlank((String) tableField.getCustomMap().get("maxLength"));
    }

    public static boolean shouldAddNotBlankAnno(TableField tableField) {
        Map<String, Object> customMap = tableField.getCustomMap();
        boolean isNullable = (boolean) customMap.get("isNullable");
        String maxLength = (String) customMap.get("maxLength");
        return !isNullable && StringUtils.isNotBlank(maxLength) && !tableField.isKeyFlag();
    }

    public static boolean shouldAddNotNullAnno(TableField tableField) {
        Map<String, Object> customMap = tableField.getCustomMap();
        boolean isNullable = (boolean) customMap.get("isNullable");
        String maxLength = (String) customMap.get("maxLength");
        return !isNullable && StringUtils.isBlank(maxLength) && !tableField.isKeyFlag();
    }

    public static boolean isVarchar(TableField tableField) {
        return tableField.getType().contains("char");
    }

    @Test
    public void test() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println("select cast(so.name as varchar(500)) as TABLE_NAME, " +
                "cast(sep.value as varchar(500)) as COMMENTS from sysobjects so " +
                "left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0 " +
                "where (xtype='U' or xtype='v')");
        System.out.println(
                "SELECT  cast(a.NAME AS VARCHAR(500)) AS TABLE_NAME,cast(b.NAME AS VARCHAR(500)) AS COLUMN_NAME, "
                        +
                        "cast(c.VALUE AS VARCHAR(500)) AS COMMENTS,cast(sys.types.NAME AS VARCHAR (500)) AS DATA_TYPE,"
                        + "(" + " SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END"
                        + " FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes "
                        +
                        " WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (A.NAME) AND sysobjects.xtype = 'PK'"
                        + " AND sysobjects.parent_obj = syscolumns.id " + " AND sysindexes.id = syscolumns.id "
                        + " AND sysobjects.NAME = sysindexes.NAME AND sysindexkeys.id = syscolumns.id "
                        + " AND sysindexkeys.indid = sysindexes.indid "
                        + " AND syscolumns.colid = sysindexkeys.colid AND syscolumns.NAME = B.NAME) as 'KEY',"
                        + "  b.is_identity isIdentity "
                        +
                        " FROM ( select name,object_id from sys.tables UNION all select name,object_id from sys.views ) a "
                        + " INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id "
                        + " LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id   "
                        +
                        " LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id "
                        + " WHERE a.NAME = '%s' and sys.types.NAME !='sysname' ");
    }

}
