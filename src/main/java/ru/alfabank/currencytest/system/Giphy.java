package ru.alfabank.currencytest.system;

import lombok.Data;

/**
 * Настройки клиента для giphy.com
 */
@Data
public class Giphy {
    private String addr;
    private String apiKey;
    private String searchLimit;
}
