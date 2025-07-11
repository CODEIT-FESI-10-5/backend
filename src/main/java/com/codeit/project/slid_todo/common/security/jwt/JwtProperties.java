package com.codeit.project.slid_todo.common.security.jwt;

import com.codeit.project.slid_todo.common.config.property.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.jwt")
@PropertySource(value = "classpath:application-local.yml", factory = YamlPropertySourceFactory.class)
public class JwtProperties {
    public int accessExpirationTime;
    public int refreshExpirationTime;
    public String authoritiesKey;
    public String tokenPrefix;
    public String secretKey;
}
