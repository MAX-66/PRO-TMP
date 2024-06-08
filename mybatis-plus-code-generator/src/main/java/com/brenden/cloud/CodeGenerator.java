package com.brenden.cloud;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ResourceBundle;

/**
 * <p>
 *
 * </p>
 *
 * @author brenden
 * @since 2023/8/8
 */
public class CodeGenerator extends BaseGenerator{


    public static void main(String[] args) {
        final ResourceBundle rb = ResourceBundle.getBundle("mybatis-plus");
        DataSourceConfig dsc = new DataSourceConfig
                .Builder(rb.getString("datasource.url"), rb.getString("datasource.userName"), rb.getString("datasource.password"))
//                .schema(rb.getString("datasource.schemaname"))
                .build();

        AutoGenerator generator = new AutoGenerator(dsc);
        generator.strategy(strategyConfig(rb).build());
        generator.global(globalConfig(rb).build());
        generator.packageInfo(packageConfig(rb).build());
        generator.template(templateConfig().build());
        generator.injection(injectionConfig().build());
        generator.execute(new FreemarkerTemplateEngine());

    }

}
