package com.brenden.cloud.core.utils;

import com.brenden.cloud.base.constant.SpecialCharacters;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/3
 */
public class SignUtil {


    public static final String KEY_SIGN = "signature";

    public static final String KEY_TIMESTAMP = "timestamp";

    public static final String KEY_NAME = "key";


    public static String sign(Map<String,Object> map, String key) {
        return sign(mapToStringAppendKey(map, key));
    }

    public static String sign(String str) {
        return EncryptionUtil.encryptMD5(str);
    }


    public static String mapToStringAppendKey(Map<String,Object> map, String key) {
        StringBuilder sb = mapToString(map);
        sb.append(KEY_NAME).append(SpecialCharacters.EQUALS_SIGN).append(key);
        return sb.toString();
    }

    public static StringBuilder mapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        List<String> collect = keySet.stream().sorted().filter(e -> !e.equals(KEY_SIGN)).toList();
        StringBuilder sb = new StringBuilder();
        for (String str : collect) {
            Object value = map.get(str);
            if (Objects.isNull(value)) {
                continue;
            }
            sb.append(str);
            sb.append(SpecialCharacters.EQUALS_SIGN);
            sb.append(value).append(SpecialCharacters.AMPERSAND);
        }
        return sb;
    }

}
