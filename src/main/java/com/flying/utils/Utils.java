package com.flying.utils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class Utils {
    /**
     * 用于获取数据库(这里便是SQLServer)字段null属性的key
     */
    public static final String CUSTOM_KEY_IS_NULLABLE = "isNullable";
    /**
     * 用于获取Mysql数据库字段null属性的key
     */
    public static final String MYSQL_CUSTOM_KEY_IS_NULLABLE = "Null";
    /**
     * 用于获取数据库(这里便是SQLServer)字段最大长度的key(这里便是SQLServer)
     */
    public static final String CUSTOM_KEY_MAX_LENGTH = "maxLength";

    /**
     * 获取Mysql数据库字段类型对应的JDBC类型
     */
    public static final MySqlJdbcTypeConvert MY_SQL_JDBC_TYPE_CONVERT = new MySqlJdbcTypeConvert();
    /**
     * 获取SqlServer数据库字段类型对应的JDBC类型
     */
    public static final SqlServerJdbcTypeConvert SQL_SERVER_JDBC_TYPE_CONVERT = new SqlServerJdbcTypeConvert();

    /**
     * 根据以下划线“_”方式命名的表名获取该表名对应的java包名。如student_score返回student.score
     *
     * @param tableName 传入的表名
     * @return
     */
    public static String getPkgFromUnderline(String tableName) {
        return tableName.replace("_", ".").toLowerCase();
    }

    /**
     * 根据以驼峰法方式命名的表名获取该表名对应的java包名。如studentScore返回student.score
     *
     * @param tableName 传入的表名
     * @return
     */
    public static String getPkgFromCamelCase(String tableName) {
        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(tableName), ".").toLowerCase();
    }

    /**
     * 根据表名获取该表名对应的java包名,支持以下划线“_”或者驼峰法的格式命名的表名,如student_score返回student.score
     *
     * @param tableName 传入的表名
     * @return
     */
    public static String getPkg(String tableName) {
        return tableName.contains("_") ? getPkgFromUnderline(tableName) : getPkgFromCamelCase(tableName);
    }

    /**
     * 根据表名获取该表名对应的路径,支持以下划线“_”或者驼峰法的格式命名的表名,如student_score返回student/score
     * 注意：返回的路径分隔符使用File.separator获取
     *
     * @param tableName 传入的表名
     * @return
     */
    public static String getSourcePath(String tableName) {
        return getPkg(tableName).replace(".", File.separator);
    }

    /**
     * 校正传入的path的分隔符,如路径为com//flying\/dao，将会返回com/flying/dao
     * 注意：返回的路径分隔符使用File.separator获取
     *
     * @param path 传入的需要被校正的路径
     * @return
     */
    public static String correctPath(String path) {
        return path.replace("//", File.separator).replace("\\\\", File.separator)
                .replace("/\\", File.separator).replace("\\/", File.separator);
    }

    /**
     * 根据传入的包名获取到该包名对应的路径,如com.flying.dao,返回com/flying/dao。
     * 注意：返回的路径分隔符使用File.separator获取
     *
     * @param pkgName 包名
     * @return
     */
    public static String getSourcePathByPkg(String pkgName) {
        return pkgName.replace(".", File.separator);
    }

    /**
     * 将传入name首字母小写然后返回
     *
     * @param name 需要被处理的字符串
     * @return
     */
    public static String makeFirstLetterLowerCaseOf(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    /**
     * 获取全类名的简单类名
     *
     * @param fullName 全类名
     * @return
     */
    public static String getClassSimpleNameFromFullName(String fullName) {
        return fullName.substring(-1 == fullName.lastIndexOf(".") ? 0 : fullName.lastIndexOf(".") + 1);
    }

    /**
     * 主要加上javax.validation.constraints.*的校验注解(目前主要@Size,@NotNull,@NotBlank)，
     * 移除Mybatis的com.baomidou.mybatisplus.annotation.*注解
     *
     * @param tableInfo        数据库表信息对象
     * @param needAddOtherAnno 当为true时才会根据情况加上@Size,@NotNull,@NotBlank等注解,否则只会移除Mybatis自己的注解
     */
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

    /**
     * 获取数据库字段(仅当为字符类型才返回,如char,nvarchar,varchar等,不包括text等类型)的长度
     *
     * @param tableField 数据库字段对象
     * @return
     */
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

    /**
     * 判断该数据库字段对应的java属性是否需要添加@Size注解。
     * 当该属性不为null且对应数据库字段类型为char,nvarchar,varchar等,不包括text等类型且该属性不为主键时才允许添加该注解
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static boolean shouldAddSizeAnno(TableField tableField) {
        return StringUtils.isNotBlank(getMaxLengthOfStr(tableField));
    }

    /**
     * 判断该数据库字段对应的java属性是否需要添加@NotBlank注解。
     * 当该属性不为null且对应java类型为String且该属性不为主键时才允许添加该注解
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static boolean shouldAddNotBlankAnno(TableField tableField) {
        return !isNullable(tableField) && isString(tableField) && !tableField.isKeyFlag();
    }

    /**
     * 判断该数据库字段对应的java属性是否需要添加@NotNull注解。
     * 当该属性不为null且对应java类型不为String且该属性不为主键时才允许添加该注解
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static boolean shouldAddNotNullAnno(TableField tableField) {
        return !isNullable(tableField) && !isString(tableField) && !tableField.isKeyFlag();
    }

    /**
     * 判断该数据库字段是否可以为null
     *
     * @param tableField 数据库字段对象
     * @return
     */
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

    /**
     * 判断该数据库字段类型是否为字符类型(如char,nvarchar,varchar等,不包括text等类型)
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static boolean isVarchar(TableField tableField) {
        return tableField.getType().contains("char");
    }

    /**
     * 判断该数据库字段类型对应的java数据类型是否是String
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static boolean isString(TableField tableField) {
        return DbColumnType.STRING.getType().equals(tableField.getColumnType().getType());
    }

    /**
     * 获取MySQL数据库字段类型对应的Jdbc类型
     *
     * @param cloumnType MySQL数据库字段类型
     * @return
     */
    public static String getMysqlJdbcType(String cloumnType) {
        JDBCType jdbcType = MY_SQL_JDBC_TYPE_CONVERT.convert(cloumnType);
        return null == jdbcType ? "" : jdbcType.getName();
    }

    /**
     * 获取SqlServer数据库字段类型对应的Jdbc类型
     *
     * @param cloumnType SqlServer数据库字段类型
     * @return
     */
    public static String getSqlServerJdbcType(String cloumnType) {
        JDBCType jdbcType = SQL_SERVER_JDBC_TYPE_CONVERT.convert(cloumnType);
        return null == jdbcType ? "" : jdbcType.getName();
    }

    /**
     * 数据库表名映射实体名时是否移除表名前缀,
     * 若需要移除则如hdic_checklist_catalog映射成ChecklistCatalog而不是HdicChecklistCatalog
     * 注意:前缀根据表名的下划线"_"做分割并取第一个,若分割后的字符串数组长度&lt=1则表示没有前缀,结果不做处理
     *
     * @param tableInfo   数据库表信息对象
     * @param isRemovePre 是否移除前缀
     * @return 返回被移除前缀的实体名(若不需要移除则不做处理)
     */
    public static String rmpreu(TableInfo tableInfo, boolean isRemovePre) {
        String result = tableInfo.getEntityName();
        if (isRemovePre) {
            String[] strings = tableInfo.getName().split("_");
            if (strings.length > 1) {
                //表示有前缀
                String entityNamePrefix = strings[0].substring(0, 1).toUpperCase() + strings[0].substring(1);
                if (tableInfo.getEntityName().startsWith(entityNamePrefix)) {
                    result = tableInfo.getEntityName().replaceFirst(entityNamePrefix, "");
                }
            }
        }
        return result;
    }

    /**
     * 获取移除前缀的表名,仅支持以下划线“_”的格式命名的表名
     *
     * @param tableName 原始表名
     * @return
     */
    public static String getTableNameRemovedPrefix(String tableName) {
        String result = tableName;
        String[] strings = tableName.split("_");
        if (strings.length > 1) {
            //表示有前缀
            result = StringUtils.join(ArrayUtils.remove(strings, 0), "_");
        }
        return result;
    }

    /**
     * 根据表名获取该表名对应的路径,当不需要移除前缀时支持以下划线“_”或者驼峰法的格式命名的表名,如student_score返回student/score,
     * 当需要移除前缀时,则仅支持以下划线“_”的格式命名的表名,因为判断前缀是采用的"_"判断,如hdic_checklist_catalog返回checklist/catalog
     * 注意：返回的路径分隔符使用File.separator获取
     *
     * @param tableName   传入的表名
     * @param isRemovePre 是否移除前缀
     * @return
     */
    public static String getSourcePath(String tableName, boolean isRemovePre) {
        return getSourcePath(!isRemovePre ? tableName : getTableNameRemovedPrefix(tableName));
    }

    /**
     * 根据以下划线“_”方式命名的表名获取该表名对应的java包名。
     * 若不需要移除前缀则如student_score返回student.score,
     * 若需要移除前缀则如hdic_checklist_catalog返回checklist.catalog
     *
     * @param tableName   传入的表名
     * @param isRemovePre 是否移除前缀
     * @return
     */
    public static String getPkgFromUnderline(String tableName, boolean isRemovePre) {
        return getPkgFromUnderline(!isRemovePre ? tableName : getTableNameRemovedPrefix(tableName));
    }

    /**
     * 判断一些字段命名的特殊情况，返回正确的属性名.
     * 如若字段是布尔值,命名为isGB,则返回的属性名是gB(这时get,set方法名是getgB,setgB),
     * 若字段是其他类型,命名为pName,则返回的属性名是pName(这时get,set方法名是getpName,setpName)
     *
     * @param tableField 数据库字段对象
     * @return
     */
    public static String getRightPropertyName(TableField tableField) {
        String propertyName = tableField.getPropertyName();
        String capitalName = tableField.getCapitalName();
        if (Character.isLowerCase(capitalName.toCharArray()[0])) {
            propertyName = capitalName;
        } else if (DbColumnType.BOOLEAN.getType().equals(tableField.getColumnType().getType()) &&
                Character.isUpperCase(capitalName.toCharArray()[0])) {
            propertyName = capitalName.substring(0, 1).toLowerCase() + capitalName.substring(1);
        }
        return propertyName;
    }

}
