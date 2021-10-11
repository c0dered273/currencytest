package ru.alfabank.currencytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Сервис анализа разницы курсов валют.
 */
@SpringBootApplication
@EnableFeignClients
@ConfigurationPropertiesScan("ru.alfabank.currencytest.system")
public class CurrencytestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencytestApplication.class, args);
    }

}
