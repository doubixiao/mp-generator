package top.itkaoti.develop;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.util.*;

/**
 * blade的代码生成精简
 */
public class Main {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("author", "LMQ");

        String url = "jdbc:mysql://56104ip:3306/pic";
        String username = "root";
        String password = "root";
        String servicePackage = "";
        String packageName = "top.itkaoti.pic.common"; // 父包名
        String tablePrefix = ""; // 表前缀过滤
        String outputDir = "Z:/data/"; // 输出目录

        /**
         * 需要生成的表名(两者只能取其一)
         */
        String[] includeTables = {"suggest"};
        /**
         * 需要排除的表名(两者只能取其一)
         */
        String[] excludeTables = {};

        Map<String, Object> customMap = new HashMap<>(11);
        customMap.put("servicePackage", servicePackage);
        customMap.put("servicePackageLowerCase", servicePackage.toLowerCase());

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author(props.getProperty("author")).dateType(DateType.TIME_PACK).enableSwagger().outputDir(outputDir).disableOpenDir())
                .packageConfig(builder -> builder.parent(packageName).controller("controller").entity("entity").service("service").serviceImpl("service.impl").mapper("mapper").xml("mapper"))
                .strategyConfig(builder -> builder.addTablePrefix(tablePrefix).addInclude(includeTables).addExclude(excludeTables)
                        .entityBuilder().naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel).enableLombok()/*.superClass("BaseEntity")*//*.addSuperEntityColumns(superEntityColumns)*/.enableFileOverride()
                        .serviceBuilder().superServiceClass("com.baomidou.mybatisplus.extension.service.IService").superServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl").formatServiceFileName("I%sService").formatServiceImplFileName("%sServiceImpl").enableFileOverride()
                        .mapperBuilder().mapperAnnotation(Mapper.class).enableBaseResultMap().enableBaseColumnList().formatMapperFileName("%sMapper").formatXmlFileName("%sMapper").enableFileOverride()
//                        .controllerBuilder()/*.superClass("BladeController")*/.formatFileName("%sController").enableRestStyle().enableHyphenStyle().enableFileOverride()
                )
                .templateConfig(builder -> builder.disable(TemplateType.ENTITY)
                        .entity("/templates/entity.java.vm")
                        .service("/templates/service.java.vm")
                        .serviceImpl("/templates/serviceImpl.java.vm")
                        .mapper("/templates/mapper.java.vm")
                        .xml("/templates/mapper.xml.vm")
//                        .controller("/templates/controller.java.vm")
                )
                .injectionConfig(builder -> builder.beforeOutputFile(
                                (tableInfo, objectMap) -> System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size())
                        )
//                        .customMap(customMap).customFile(customFile)
                )
                .templateEngine(new VelocityTemplateEngine() {
                    @Override
                    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
                        String entityName = tableInfo.getEntityName();
                        String entityNameLower = tableInfo.getEntityName().toLowerCase();

                        customFiles.forEach(customFile -> {
                            String key = customFile.getFileName();
                            String value = customFile.getTemplatePath();
                            String outputPath = getPathInfo(OutputFile.parent);
                            objectMap.put("entityKey", entityNameLower);
                            outputFile(new File(String.valueOf(outputPath)), objectMap, value, Boolean.TRUE);
                        });
                    }
                })
                .execute();

    }
}