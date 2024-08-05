package com.brenden.cloud.openfeign.error;

import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalCodeEnum;
import com.brenden.cloud.base.error.GlobalException;
import com.brenden.cloud.core.utils.JacksonUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * feign 异常调用处理
 * </p>
 *
 * @author lxq
 * @since 2024/8/5
 */
@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            log.error("Feign service call failed, invoked method: {}, reason: {}", methodKey, response);
            ResultEntity<?> resultEntity = JacksonUtil.getObjectMapper().readValue(response.body().asInputStream(), ResultEntity.class);
            GlobalCodeEnum codeEnum = GlobalCodeEnum.getEnum(resultEntity.getResultCode());
            if (ObjectUtils.isEmpty(codeEnum)) {
                throw new GlobalException(GlobalCodeEnum.GC_800011);
            }
            throw new GlobalException(codeEnum);
        } catch (IOException e) {
            // 处理读取响应体时的异常
            log.error("Failed to read response body from {}", methodKey, e);
        }
        return new ErrorDecoder.Default().decode(methodKey, response);
    }

}
