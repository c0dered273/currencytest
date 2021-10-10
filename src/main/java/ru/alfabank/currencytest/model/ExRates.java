package ru.alfabank.currencytest.model;

import java.util.Map;

public record ExRates(String disclaimer,
                      String license,
                      Long timestamp,
                      String base,
                      Map<String, Double> rates) {
}
