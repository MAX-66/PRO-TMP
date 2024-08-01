package com.brenden.cloud.base.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.brenden.cloud.base.entity.BaseEntity;

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

    public final static ThreadLocal<BaseEntity> USER_CONTEXT = new TransmittableThreadLocal<>();

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
    public static BaseEntity getContext() {
        return Optional.ofNullable(USER_CONTEXT.get()).orElse(new BaseEntity());
    }


    /**
     * 设置上下文用户信息
     * @param context 用户信息
     */
    public static void setContext(BaseEntity context) {
        USER_CONTEXT.set(context);
    }
}
