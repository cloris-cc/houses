package org.cloris.houses.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Jackson Fang
 * Date:   2018/11/13
 * Time:   15:51
 */
@SpringBootApplication
@EnableAdminServer
public class SpringAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAdminApplication.class, args);
    }
}
