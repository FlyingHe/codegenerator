package com.flying.codegenerator.dah.sqlserver;

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
        dsc.setUrl("jdbc:sqlserver://192.167.6.16:1433;databasename=panda-duty");
        dsc.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dsc.setUsername("sa");
        dsc.setPassword("ciotea@2017");
        dsc.setDbQuery(new ISqlServerQuery());
        mpg.setDataSource(dsc);

        // 自定义配置
        //实体类
        final String pojoPkg = "com.flying.test.domain";
        boolean pojoPkgSuffix = true;
        //QO
        final String pojoQOPkg = "com.flying.test.domain";
        boolean pojoQOPkgSuffix = true;
        //Service
        final String pojoServicePkg = "com.flying.test.domain";
        boolean pojoServicePkgSuffix = true;
        //Mapper接口
        final String mapperPkg = "com.flying.test.domain";
        boolean mapperPkgSuffix = true;
        //Mapper XML
        final String mapperXmlPkg = "com.flying.test.domain";
        boolean mapperXmlPkgSuffix = true;

        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("utils", new Utils());
                map.put("date", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
                map.put("ognl", "com.github.flyinghe.tools.Ognl");
                map.put("baseMapper", "com.flying.codegenerator.dah.sqlserver.BaseMapper");
                map.put("baseService", "com.flying.codegenerator.dah.sqlserver.BaseService");
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
        String projectPath = System.getProperty("user.dir");
//        String projectPath = "D:\\WORKSPACE\\intelljIdea\\dinganhua\\panda\\code-panda\\panda-service-enforce";
        List<FileOutConfig> focList = new ArrayList<>();
        //实体类
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo, true);
                return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                        projectPath, Utils.getSourcePathByPkg(pojoPkg),
                        pojoPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                        tableInfo.getEntityName(), StringPool.DOT_JAVA));
            }
        });
        //Mapper接口
        focList.add(new FileOutConfig("/templates/dah/sqlServer/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                        projectPath, Utils.getSourcePathByPkg(mapperPkg),
                        mapperPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                        tableInfo.getMapperName(), StringPool.DOT_JAVA));
            }
        });
        //Mapper XML
        focList.add(new FileOutConfig("/templates/dah/sqlServer/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                        projectPath, Utils.getSourcePathByPkg(mapperXmlPkg),
                        mapperXmlPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                        tableInfo.getXmlName(), StringPool.DOT_XML));
            }
        });
        //QO
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojoQO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo, true);
                return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                        projectPath, Utils.getSourcePathByPkg(pojoQOPkg),
                        pojoQOPkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                        tableInfo.getEntityName() + "QO", StringPool.DOT_JAVA));
            }
        });
        //Service
        focList.add(new FileOutConfig("/templates/dah/sqlServer/pojoService.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return Utils.correctPath(String.format("%s/src/main/java/%s/%s/%s%s",
                        projectPath, Utils.getSourcePathByPkg(pojoServicePkg),
                        pojoServicePkgSuffix ? Utils.getSourcePath(tableInfo.getName()) : "",
                        tableInfo.getEntityName() + "Service", StringPool.DOT_JAVA));
            }
        });

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
        strategy.setInclude("archive_organization_accident");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
