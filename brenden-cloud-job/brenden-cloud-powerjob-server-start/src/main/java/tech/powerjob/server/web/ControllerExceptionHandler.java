package tech.powerjob.server.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.powerjob.common.exception.PowerJobException;
import tech.powerjob.common.response.PowerResultDTO;

/**
 * 统一处理 web 层异常信息
 *
 * @author tjq
 * @since 2020/3/30
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public PowerResultDTO<Void> exceptionHandler(Exception e) {

        PowerResultDTO<Void> ret = PowerResultDTO.f(ExceptionUtils.getMessage(e));

        // 不是所有异常都需要打印完整堆栈，后续可以定义内部的Exception，便于判断
        if (e instanceof PowerJobException) {
            ret.setCode(((PowerJobException) e).getCode());
            log.warn("[ControllerException] PowerJobException, message is {}.", e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            log.warn("[ControllerException] http request failed due to IllegalArgument, message is {}.", e.getMessage());
        } else if (e instanceof HttpMessageNotReadableException || e instanceof MethodArgumentTypeMismatchException) {
            log.warn("[ControllerException] invalid http request params, exception is {}.", e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            log.warn("[ControllerException] invalid http request method, exception is {}.", e.getMessage());
        } else {
            log.error("[ControllerException] http request failed.", e);
        }

        return ret;
    }
}
