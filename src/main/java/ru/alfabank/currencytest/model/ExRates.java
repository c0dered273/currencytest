package ru.alfabank.currencytest.model;

import java.util.Map;

/**
 * Ответ openexchangerates.org о курсах валют.
 */
public record ExRates(String disclaimer,
                      String license,
                      Long timestamp,
                      String base,
                      Map<String, Double> rates) {
}
