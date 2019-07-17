package com.flying.codegenerator.dah.mysql;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.flying.sql.query.IMySqlQuery;
import com.flying.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class CodeGeneratorMysql {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor("FlyingHe");
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.167.6.16:3306/ssp");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("ciotea@db");
        dsc.setDbQuery(new IMySqlQuery());
        mpg.setDataSource(dsc);

        // 自定义配置
        //是否移除前缀
        boolean isRemovePrefix = false;
        //实体类
        final String pojoPkg = "com.flying.test.risksite";
        boolean pojoPkgSuffix = true;
        //QO
        final String pojoQOPkg = "com.flying.test.risksite";
        boolean pojoQOPkgSuffix = true;
        //Service
        final String pojoServicePkg = "com.flying.test.risksite";
        boolean pojoServicePkgSuffix = true;
        //Mapper接口
        final String mapperPkg = "com.flying.test.risksite";
        boolean mapperPkgSuffix = true;
        //Mapper XML
        final String mapperXmlPkg = "com.flying.test.risksite";
        boolean mapperXmlPkgSuffix = true;

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("utils", new Utils());
                map.put("date", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
                //标识数据库表名映射实体名时是否移除表名前缀,
                //如hdic_checklist_catalog映射成ChecklistCatalog而不是HdicChecklistCatalog
                map.put("rmpre", isRemovePrefix);
                map.put("ognl", "com.github.flyinghe.tools.Ognl");
                map.put("baseMapper", "com.ciotea.zues.infrastructure.util.BaseRepo");
                map.put("baseService", "com.ciotea.zues.infrastructure.util.BaseService");
                map.put("pojoPkg", pojoPkg);
                map.put("pojoPkgSuffix", pojoPkgSuffix);
                map.put("pojoQOPkg", pojoQOPkg);
                map.put("pojoQOPkgSuffix", pojoQOPkgSuffix);
                map.put("pojoServicePkg", pojoServicePkg);
                map.put("pojoServicePkgSuffix", pojoServicePkgSuffix);
                map.put("mapperPkg", mapperPkg);
                map.put("mapperPkgSuffix", mapperPkgSuffix);
                this.setMap(map);
            }
        };
        // 自定义输出配置
//        String projectPath = System.getProperty("user.dir");
        String projectPath = "D:\\WORKSPACE\\intelljIdea\\codegenerator";
        List<FileOutConfig> focList = new ArrayList<>();
        //实体类
        if (StringUtils.isNotBlank(pojoPkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/pojo.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    Utils.handleImportPkgs(tableInfo, false);
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(pojoPkg),
                            pojoPkgSuffix ? Utils.getSourcePath(tableInfo.getName(), isRemovePrefix) : "",
                            Utils.rmpreu(tableInfo, isRemovePrefix), StringPool.DOT_JAVA));
                }
            });
        }
        //Mapper接口
        if (StringUtils.isNotBlank(mapperPkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/mapper.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(mapperPkg),
                            mapperPkgSuffix ? Utils.getSourcePath(tableInfo.getName(), isRemovePrefix) : "",
                            Utils.rmpreu(tableInfo, isRemovePrefix) + "Repo", StringPool.DOT_JAVA));
                }
            });
        }
        //Mapper XML
        if (StringUtils.isNotBlank(mapperXmlPkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/mapper.xml.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(mapperXmlPkg),
                            mapperXmlPkgSuffix ? Utils.getSourcePath(tableInfo.getName(), isRemovePrefix) : "",
                            Utils.rmpreu(tableInfo, isRemovePrefix) + "Mapper", StringPool.DOT_XML));
                }
            });
        }
        //QO
        if (StringUtils.isNotBlank(pojoQOPkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/pojoQO.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    Utils.handleImportPkgs(tableInfo, false);
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(pojoQOPkg),
                            pojoQOPkgSuffix ? Utils.getSourcePath(tableInfo.getName(), isRemovePrefix) : "",
                            Utils.rmpreu(tableInfo, isRemovePrefix) + "QO", StringPool.DOT_JAVA));
                }
            });
        }
        //Service
        if (StringUtils.isNotBlank(pojoServicePkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/pojoService.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(pojoServicePkg),
                            pojoServicePkgSuffix ? Utils.getSourcePath(tableInfo.getName(), isRemovePrefix) : "",
                            Utils.rmpreu(tableInfo, isRemovePrefix) + "Service", StringPool.DOT_JAVA));
                }
            });
        }

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板,自带的均不输出
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(null);
        templateConfig.setXml(null);
        templateConfig.setMapper(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表名到实体名的映射规则
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库字段名到实体属性名的映射规则
        strategy.setColumnNaming(NamingStrategy.no_change);
        //需要被逆向生成的数据库表,不设置则为全部
//        strategy.setInclude("riskSite_education_base_info", "riskSite_ent_base_info",
//                "riskSite_medical_institution", "riskSite_older_lookafter_equipment_base_info",
//                "riskSite_recreation_place",
//                "riskSite_stay_restaurant", "riskSite_street_enterprise", "riskSite_xindu_gas_station_base_info",
//                "riskSite_xindu_storage_base_info");
        strategy.setInclude("gb_industryClassification");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
