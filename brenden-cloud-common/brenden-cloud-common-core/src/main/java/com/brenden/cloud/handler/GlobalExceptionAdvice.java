package com.brenden.cloud.handler;

import com.brenden.cloud.entity.ResultEntity;
import com.brenden.cloud.error.GlobalCodeEnum;
import com.brenden.cloud.error.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultEntity<?> handle(RuntimeException ex) {
        log.error("runtime exception, error msg: {}", ex.getMessage());
        return ResultEntity.fail(GlobalCodeEnum.GC_800006);
    }

}
