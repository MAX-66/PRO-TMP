package com.brenden.cloud.auth.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.security.jackson2.SecurityJackson2Modules;

import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/17
 */
public class SecurityJacksonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 反序列化: JSON 字段中有Java对象中没有不报错
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 序列化: 排除值为 null 的对象
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        MAPPER.registerModule(new JavaTimeModule());


        MAPPER.registerModules(SecurityJackson2Modules.getModules(SecurityJacksonUtils.class.getClassLoader()));
    }

    @SneakyThrows
    public static <T> String toJson(T domain) {
        return MAPPER.writeValueAsString(domain);
    }

    @SneakyThrows
    public static Map<String, Object> toMap(String content) {
        JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        return MAPPER.readValue(content, javaType);
    }
}
