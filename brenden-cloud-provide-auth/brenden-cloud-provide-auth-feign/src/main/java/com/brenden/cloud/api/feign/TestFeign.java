package com.brenden.cloud.api.feign;

import com.brenden.cloud.api.hystrix.TestHystrix;
import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/4
 */
@FeignClient(value = "brenden-cloud-provide-auth-api", fallback = TestHystrix.class, url = "http://127.0.0.1:8010")
public interface TestFeign {

    @GetMapping("/feign-test/test")
    ResultEntity<BaseEntity> feign(@SpringQueryMap BaseEntity entity);

}
