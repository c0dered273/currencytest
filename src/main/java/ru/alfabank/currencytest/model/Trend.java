package ru.alfabank.currencytest.model;

/**
 * Результат сравнения курсов валют. Если positive = true - курс валюты app.quote вырос.
 */
public record Trend(boolean positive) {
}
