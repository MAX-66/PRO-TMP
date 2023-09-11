package con.brenden.cloud;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.brenden.cloud.entity.BaseDO;

import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 基础测试类
 *
 * @author lanjerry
 * @since 3.5.3
 */
public class BaseGenerator {

    /**
     * 策略配置
     */
    protected static StrategyConfig.Builder strategyConfig(ResourceBundle rb) {
        StrategyConfig.Builder strategy = new StrategyConfig.Builder();
        strategy.addInclude(rb.getString("tables.name").split(","))
                .addTablePrefix(rb.getString("tables.prefix"));
        strategy.entityBuilder().superClass(BaseDO.class)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .enableTableFieldAnnotation()
                .formatFileName("%sDO")
                .logicDeleteColumnName("is_delete")
                .versionColumnName("version");
        strategy.serviceBuilder().formatServiceFileName("%sManager").formatServiceImplFileName("%sManagerImpl");
        strategy.controllerBuilder().formatFileName("%sController").enableRestStyle();
        strategy.mapperBuilder().superClass(BaseMapper.class)
                .formatMapperFileName("%sMapper")
                .enableMapperAnnotation()
                .enableBaseResultMap()
                .enableBaseColumnList()
                .formatXmlFileName("%sMapper");
        return strategy;
    }

    /**
     * 全局配置
     */
    protected static GlobalConfig.Builder globalConfig(ResourceBundle rb) {
        String projectPath = System.getProperty("user.dir");
        GlobalConfig.Builder gc = new GlobalConfig.Builder();
        gc.outputDir(projectPath + "/src/main/java");
        gc.author(rb.getString("author"));
        gc.disableOpenDir();
        return gc;
    }

    /**
     * 包配置
     */
    protected static PackageConfig.Builder packageConfig(ResourceBundle rb) {
        PackageConfig.Builder pc = new PackageConfig.Builder();
        pc.parent(rb.getString("module.package"));
        pc.moduleName(rb.getString("module.name"));
        pc.controller("controller");
        pc.entity("persistence.domain");
        pc.mapper("persistence.mapper");
        pc.xml("sqlmap");
        pc.service("persistence.manager");
        pc.serviceImpl("persistence.manager.impl");
        Map<OutputFile, String> outputFileStringMap = Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/sqlmap/");
        pc.pathInfo(outputFileStringMap);
        return pc;
    }

    /**
     * 模板配置
     */
    protected static TemplateConfig.Builder templateConfig() {
        TemplateConfig.Builder tc = new TemplateConfig.Builder();
        tc.entity("entity.java");
        return tc;
    }

    /**
     * 注入配置
     */
    protected static InjectionConfig.Builder injectionConfig() {
        // 测试自定义输出文件之前注入操作，该操作再执行生成代码前 debug 查看
        InjectionConfig.Builder builder = new InjectionConfig.Builder();
        return builder.beforeOutputFile((tableInfo, objectMap) -> {
            System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
        });
    }
}
