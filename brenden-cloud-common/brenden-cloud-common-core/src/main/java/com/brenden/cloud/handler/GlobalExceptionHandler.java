package com.brenden.cloud.handler;

import com.brenden.cloud.entity.ResultEntity;
import com.brenden.cloud.error.GlobalException;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResultEntity<?> handler(GlobalException exception) {
        log.error("global exception, error code: {}, error msg: {}", exception.getErrorCode(), exception.getErrorMsg());
        return ResultEntity.fail(exception.getErrorCode(), exception.getErrorMsg());
    }


    @ExceptionHandler(Exception.class)
    public ResultEntity<?> handler(Exception exception) {
        log.error("system exception, error msg: {}", exception.getMessage());
        return ResultEntity.fail(500, exception.getMessage());
    }

}
