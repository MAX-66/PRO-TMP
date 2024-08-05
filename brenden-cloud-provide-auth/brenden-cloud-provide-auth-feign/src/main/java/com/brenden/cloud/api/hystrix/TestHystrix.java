package com.brenden.cloud.api.hystrix;

import com.brenden.cloud.api.feign.TestFeign;
import com.brenden.cloud.base.entity.BaseEntity;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.base.error.GlobalException;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/4
 */
@Component
public class TestHystrix implements TestFeign {
    @Override
    public ResultEntity<BaseEntity> feign(BaseEntity entity) {
        throw new GlobalException(GlobalCodeEnum.GC_800005);
    }
}
