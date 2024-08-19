package tech.powerjob.server.persistence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/17
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.core")
public class OmsRemoteDatasourceProperties {

    private String driverClassName;
    private String jdbcUrl;
    private String username;
    private String password;
}
