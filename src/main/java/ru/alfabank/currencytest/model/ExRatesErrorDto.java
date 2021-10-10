package ru.alfabank.currencytest.model;

public record ExRatesErrorDto(Boolean error,
                              Integer status,
                              String message,
                              String description) {
}
