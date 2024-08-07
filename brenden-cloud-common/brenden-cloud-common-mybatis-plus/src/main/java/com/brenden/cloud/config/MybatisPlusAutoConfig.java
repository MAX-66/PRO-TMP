package com.brenden.cloud.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.brenden.cloud.injector.LogicSqlInjector;
import lombok.SneakyThrows;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;

/**
 * <p>
 *
 * </p>
 *
 * @author brenden
 * @since 2023/8/8
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.brenden.cloud.**.persistence.mapper")
public class MybatisPlusAutoConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }


    @Bean
    @SneakyThrows
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(InetAddress.getLocalHost());
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * 解除每页500条限制, 在超出页数之后mybatis会自动断掉
     */
    private PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        // 单页分页条数限制
        interceptor.setMaxLimit(-1L);
        interceptor.setOverflow(true);
        return interceptor;
    }
}
