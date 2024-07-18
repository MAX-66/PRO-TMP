package com.brenden.cloud.core.handler;

import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.base.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/6/10
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(GlobalException.class)
    public ResultEntity<?> handler(GlobalException exception) {
        log.error("global exception, error code: {}, error msg: {}", exception.getErrorCode(), exception.getErrorMsg());
        return ResultEntity.fail(exception.getErrorCode(), exception.getErrorMsg());
    }

}
