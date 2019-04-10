package com.flying.codegenerator.dah;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.flying.sql.query.ISqlServerQuery;
import com.flying.utils.Utils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class CodeGeneratorSqlServer {

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
        dsc.setUrl("jdbc:sqlserver://192.167.6.16:1433;databasename=panda-hd");
        dsc.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dsc.setUsername("sa");
        dsc.setPassword("ciotea@2017");
        dsc.setDbQuery(new ISqlServerQuery());
        mpg.setDataSource(dsc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("utils", new Utils());
                map.put("date", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
                map.put("ognl", "com.github.flyinghe.tools.Ognl");
                map.put("baseMapper", "com.flying.utils.BaseMapperWithoutTableSuffix");
                map.put("pojoPkg", "com.flying.test");
                map.put("pojoPkgSuffix", true);
                map.put("pojoQOPkg", "com.flying.test");
                map.put("pojoQOPkgSuffix", true);
                map.put("pojoServicePkg", "com.flying.test");
                map.put("pojoServicePkgSuffix", true);
                map.put("mapperPkg", "com.flying.test");
                map.put("mapperPkgSuffix", true);
                this.setMap(map);
            }
        };
        // 自定义输出配置
        String projectPath = System.getProperty("user.dir");
        List<FileOutConfig> focList = new ArrayList<>();
        //实体类
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo);
                return String.format("%s/src/main/java/com/flying/test/%s/%s%s", projectPath,
                        Utils.getSourcePath(tableInfo.getName()), tableInfo.getEntityName(), StringPool.DOT_JAVA);
            }
        });
        //Mapper接口
        focList.add(new FileOutConfig("/templates/dah/sqlServer/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/java/com/flying/test/%s/%s%s", projectPath,
                        Utils.getSourcePath(tableInfo.getName()), tableInfo.getMapperName(), StringPool.DOT_JAVA);
            }
        });
        //Mapper XML
        focList.add(new FileOutConfig("/templates/dah/sqlServer/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/java/com/flying/test/%s/%s%s", projectPath,
                        Utils.getSourcePath(tableInfo.getName()), tableInfo.getXmlName(), StringPool.DOT_XML);
            }
        });
        //QO
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojoQO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo);
                return String.format("%s/src/main/java/com/flying/test/%s/%s%s", projectPath,
                        Utils.getSourcePath(tableInfo.getName()), tableInfo.getEntityName() + "QO",
                        StringPool.DOT_JAVA);
            }
        });
        //Service
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojoService.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/java/com/flying/test/%s/%s%s", projectPath,
                        Utils.getSourcePath(tableInfo.getName()), tableInfo.getEntityName() + "Service",
                        StringPool.DOT_JAVA);
            }
        });

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
        strategy.setInclude("gov_insp_unit_record");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
