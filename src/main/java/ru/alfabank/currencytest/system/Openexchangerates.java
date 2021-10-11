package ru.alfabank.currencytest.system;

import lombok.Data;

/**
 * Настройки клиента для оpenexchangerates.org.
 */
@Data
public class Openexchangerates {
    private String addr;
    private String appId;
}
