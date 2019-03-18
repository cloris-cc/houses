package org.cloris.houses.web.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.httpclient")
@Data
public class HttpClientProperties {
    private Integer connectTimeout = 1000;
    private Integer socketTimeout = 10000;

    private String agent = "agent";
    private Integer maxConnPerRoute = 10;
    private Integer maxConnTotal = 50;
}
