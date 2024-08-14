package com.brenden.cloud.core.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/14
 */
@Slf4j
public class ResponseUtil {


    public static void response(HttpServletResponse response, Object obj) {
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try {
            response.getWriter().write(JacksonUtil.toJson(obj));
        } catch (IOException e) {
            log.error("response io error", e);
        }
    }

    public static void response(HttpServletResponse response, int status, Object obj) {
        response.setStatus(status);
        response(response, obj);
    }

}
