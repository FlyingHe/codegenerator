package com.flying.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class Utils {
    public static final String CUSTOM_KEY_IS_NULLABLE = "isNullable";
    public static final String MYSQL_CUSTOM_KEY_IS_NULLABLE = "Null";
    public static final String CUSTOM_KEY_MAX_LENGTH = "maxLength";


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

    public static void handleImportPkgs(TableInfo tableInfo, boolean needAddOtherAnno) {
        //移除Mybatis自己的注解
        List<String> excludePkg = new ArrayList<>();
        tableInfo.getImportPackages().forEach(pkg -> {
            if (pkg.startsWith("com.baomidou.mybatisplus.annotation")) {
                excludePkg.add(pkg);
            }
        });
        excludePkg.forEach(pkg -> tableInfo.getImportPackages().remove(pkg));
        if (!needAddOtherAnno) {
            return;
        }
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

    public static String getMaxLengthOfStr(TableField tableField) {
        String result = "";
        if (null != tableField.getCustomMap() && tableField.getCustomMap().containsKey(CUSTOM_KEY_MAX_LENGTH)) {
            //SqlServer
            result = (String) tableField.getCustomMap().get(CUSTOM_KEY_MAX_LENGTH);
        } else if (isVarchar(tableField)) {
            //Mysql
            int leftIndex = tableField.getType().lastIndexOf("(");
            int rightIndex = tableField.getType().lastIndexOf(")");
            if (-1 != leftIndex && -1 != rightIndex) {
                try {
                    result = String.valueOf(Integer.valueOf(tableField.getType().substring(leftIndex + 1, rightIndex)));
                } catch (NumberFormatException e) {
                    //do nothing
                }
            }
        }
        return result;
    }

    public static boolean shouldAddSizeAnno(TableField tableField) {
        return StringUtils.isNotBlank(getMaxLengthOfStr(tableField));
    }


    public static boolean shouldAddNotBlankAnno(TableField tableField) {
        return !isNullable(tableField) && isString(tableField) && !tableField.isKeyFlag();
    }

    public static boolean shouldAddNotNullAnno(TableField tableField) {
        return !isNullable(tableField) && !isString(tableField) && !tableField.isKeyFlag();
    }

    public static boolean isNullable(TableField tableField) {
        boolean isNullable = true;
        if (null != tableField.getCustomMap()) {
            if (tableField.getCustomMap().containsKey(CUSTOM_KEY_IS_NULLABLE)) {
                //SqlServer
                isNullable = (boolean) tableField.getCustomMap().get(CUSTOM_KEY_IS_NULLABLE);
            } else if (tableField.getCustomMap().containsKey(MYSQL_CUSTOM_KEY_IS_NULLABLE)) {
                //mysql
                isNullable = "YES".equalsIgnoreCase(
                        (String) tableField.getCustomMap().get(MYSQL_CUSTOM_KEY_IS_NULLABLE));
            }
        }
        return isNullable;
    }

    public static boolean isVarchar(TableField tableField) {
        return tableField.getType().contains("char");
    }

    public static boolean isString(TableField tableField) {
        return DbColumnType.STRING.getType().equals(tableField.getColumnType().getType());
    }
}
