package tech.powerjob.server.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import tech.powerjob.server.auth.Permission;

import java.lang.reflect.Method;

/**
 * 空
 *
 * @author tjq
 * @since 2024/2/12
 */
public class EmptyPlugin implements DynamicPermissionPlugin, GrantPermissionPlugin {
    @Override
    public Permission calculate(HttpServletRequest request, Object handler) {
        return null;
    }

    @Override
    public void grant(Object[] args, Object result, Method method, Object originBean) {

    }
}
