package com.brenden.cloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/12/6
 */

@SpringBootApplication
public class GateWayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(GateWayApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}
