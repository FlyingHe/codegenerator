package com.flying.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.apache.commons.lang3.StringUtils;

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

    public static String getClassSimpleNameFromFullName(String fullName) {
        return fullName.substring(-1 == fullName.lastIndexOf(".") ? 0 : fullName.lastIndexOf(".") + 1);
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

}
