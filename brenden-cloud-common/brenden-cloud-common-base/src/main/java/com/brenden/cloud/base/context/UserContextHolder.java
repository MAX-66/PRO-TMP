package com.brenden.cloud.base.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.brenden.cloud.base.entity.UserContextPayload;

import java.util.Optional;

/**
 * <p>
 *  用户上下文
 * </p>
 *
 * @author lxq
 * @since 2024/7/29
 */
public class UserContextHolder {

    public static final ThreadLocal<UserContextPayload> USER_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 清除上下文
     */
    public static void clearContext() {
        USER_CONTEXT.remove();
    }

    /**
     * 获取上下文用户信息
     * @return 用户信息
     */
    public static UserContextPayload getContext() {
        return Optional.ofNullable(USER_CONTEXT.get()).orElse(new UserContextPayload());
    }


    /**
     * 设置上下文用户信息
     * @param context 用户信息
     */
    public static void setContext(UserContextPayload context) {
        USER_CONTEXT.set(context);
    }


    /**
     * 获取当前线程用户Id
     * @return {@link Long}
     */
    public static Long getUserId() {
        return getContext().getUserId();
    }

    /**
     * 获取当前线程用户名称
     * @return {@link String}
     */
    public static String getUsername() {
        return getContext().getUsername();
    }

    /**
     * 获取当前线程用户token
     * @return {@link String}
     */
    public static String getToken() {
        return getContext().getToken();
    }
}
