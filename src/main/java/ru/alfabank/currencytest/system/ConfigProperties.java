package ru.alfabank.currencytest.system;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
    private String base;
    private String quote;
    private int deep;
    private Openexchangerates openexchangerates;
    private Giphy giphy;
}
