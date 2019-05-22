package com.flying.codegenerator.dah;

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
        //实体类
        final String pojoPkg = "com.ciotea.spg.hdic.domain";
        boolean pojoPkgSuffix = true;
        //QO
        final String pojoQOPkg = "com.ciotea.spg.hdic.domain";
        boolean pojoQOPkgSuffix = true;
        //Service
        final String pojoServicePkg = "";
        boolean pojoServicePkgSuffix = true;
        //Mapper接口
        final String mapperPkg = "";
        boolean mapperPkgSuffix = true;
        //Mapper XML
        final String mapperXmlPkg = "com.ciotea.spg.hdic.domain";
        boolean mapperXmlPkgSuffix = true;

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("utils", new Utils());
                map.put("date", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
                map.put("ognl", "com.github.flyinghe.tools.Ognl");
                map.put("baseMapper", "com.ciotea.spg.hdic.domain.util.BaseMapper");
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
        String projectPath = "C:\\Users\\Administrator\\Desktop\\ssp";
        List<FileOutConfig> focList = new ArrayList<>();
        //实体类
        if (StringUtils.isNotBlank(pojoPkg)) {
            focList.add(new FileOutConfig("/templates/dah/mysql/pojo.java.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    Utils.handleImportPkgs(tableInfo, false);
                    return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                            projectPath, Utils.getSourcePathByPkg(pojoPkg),
                            pojoPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                            tableInfo.getEntityName(), StringPool.DOT_JAVA));
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
                            mapperPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                            tableInfo.getMapperName(), StringPool.DOT_JAVA));
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
                            mapperXmlPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                            tableInfo.getXmlName(), StringPool.DOT_XML));
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
                            pojoQOPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                            tableInfo.getEntityName() + "QO", StringPool.DOT_JAVA));
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
                            pojoServicePkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                            tableInfo.getEntityName() + "Service", StringPool.DOT_JAVA));
                }
            });
        }

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
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
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.no_change);
        strategy.setInclude("checklist_type");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
