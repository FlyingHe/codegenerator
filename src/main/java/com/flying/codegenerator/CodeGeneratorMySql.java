package com.flying.codegenerator;

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
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;

/**
 * @author Administrator
 * @date 2019/4/8
 */
public class CodeGeneratorMySql {

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
        dsc.setUrl("jdbc:mysql://localhost:3306/apm");
//        dsc.setUrl("jdbc:mysql://localhost:3306/ssm");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("1007");
        dsc.setDbQuery(new IMySqlQuery());
        mpg.setDataSource(dsc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("utils", new Utils());
                map.put("date", DateFormatUtils.format(new Date(), "yyyy/MM/dd"));
                map.put("ognl", "com.github.flyinghe.tools.Ognl");
                map.put("baseMapper", "com.flying.support.BaseMapper");
//                map.put("baseMapper", "at.flying.domain.BaseMapper");
                map.put("baseService", "com.flying.support.BaseService");
//                map.put("baseService", "at.flying.domain.BaseService");
                map.put("pojoPkg", "com.flying.test.domain");
//                map.put("pojoPkg", "at.flying.domain");
                map.put("pojoPkgSuffix", false);
                map.put("pojoQOPkg", "com.flying.test.qo");
//                map.put("pojoQOPkg", "at.flying.domain");
                map.put("pojoQOPkgSuffix", false);
                map.put("pojoServicePkg", "com.flying.test.service");
//                map.put("pojoServicePkg", "at.flying.service");
                map.put("pojoServicePkgSuffix", false);
                map.put("mapperPkg", "com.flying.test.mapper");
//                map.put("mapperPkg", "at.flying.interfaces");
                map.put("mapperPkgSuffix", false);
                this.setMap(map);
            }
        };
        // 自定义输出配置
        String projectPath = System.getProperty("user.dir");
//        String projectPath = "D:\\WORKSPACE\\intelljIdea\\SSMProjectMaven";
        List<FileOutConfig> focList = new ArrayList<>();
        //实体类
        focList.add(new FileOutConfig("/templates/mysql/pojo.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo, false);
                return String.format("%s/src/main/java/com/flying/test/domain/%s%s", projectPath,
                        tableInfo.getEntityName(), StringPool.DOT_JAVA);
//                return String.format("%s/src/main/java/at/flying/domain/%s%s", projectPath,
//                        tableInfo.getEntityName(), StringPool.DOT_JAVA);
            }
        });
        //Mapper接口
        focList.add(new FileOutConfig("/templates/mysql/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/java/com/flying/test/mapper/%s%s", projectPath,
                        tableInfo.getMapperName(), StringPool.DOT_JAVA);
//                return String.format("%s/src/main/java/at/flying/interfaces/%s%s", projectPath,
//                        tableInfo.getMapperName(), StringPool.DOT_JAVA);
            }
        });
        //Mapper XML
        focList.add(new FileOutConfig("/templates/mysql/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/resources/com/flying/test/mapper/%s%s", projectPath,
                        tableInfo.getXmlName(), StringPool.DOT_XML);
//                return String.format("%s/src/main/resources/at/flying/mapper/xml/%s%s", projectPath,
//                        tableInfo.getXmlName(), StringPool.DOT_XML);
            }
        });
        //QO
        focList.add(new FileOutConfig("/templates/mysql/pojoQO.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                Utils.handleImportPkgs(tableInfo, false);
                return String.format("%s/src/main/java/com/flying/test/qo/%s%s", projectPath,
                        tableInfo.getEntityName() + "QO", StringPool.DOT_JAVA);
//                return String.format("%s/src/main/java/at/flying/domain/%s%s", projectPath,
//                        tableInfo.getEntityName() + "QO", StringPool.DOT_JAVA);
            }
        });
        //Service
        focList.add(new FileOutConfig("/templates/mysql/pojoService.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return String.format("%s/src/main/java/com/flying/test/service/%s%s", projectPath,
                        tableInfo.getEntityName() + "Service", StringPool.DOT_JAVA);
//                return String.format("%s/src/main/java/at/flying/service/%s%s", projectPath,
//                        tableInfo.getEntityName() + "Service", StringPool.DOT_JAVA);
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
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude("staff", "train_fund_recharge_record", "salary_record");
//        strategy.setInclude("student");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
