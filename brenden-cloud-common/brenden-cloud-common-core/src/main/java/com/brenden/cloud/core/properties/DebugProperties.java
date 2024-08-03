package com.brenden.cloud.core.properties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static com.brenden.cloud.base.constant.Constant.DEV_PROFILE;
import static com.brenden.cloud.base.constant.Constant.TEST_PROFILE;

/**
 * <p>
 * 环境配置类
 * </p>
 *
 * @author lxq
 * @since 2024/8/4
 */
@Data
@RequiredArgsConstructor
@Component
public class DebugProperties {

    private final ApplicationContext applicationContext;

    /**
     * 检查当前环境是否为开发或测试环境
     * @return 如果是开发或测试环境返回true，否则返回false
     */
    public boolean isDevOrTestEnv() {
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            if (DEV_PROFILE.equals(profile) || TEST_PROFILE.equals(profile)) {
                return true;
            }
        }
        return false;
    }

}
