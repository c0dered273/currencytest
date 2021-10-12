package ru.alfabank.currencytest.services;

import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.model.Trend;

/**
 * Предоставляет методы для запроса и обработки курсов валют.
 */
public interface CurrencyService {
    Trend getTrend(String base);

    ExRates getLastCurrency(String baseCurrency);

    ExRates getHistoricCurrency(String date, String baseCurrency);

    String getHistoricalDate(String pattern);
}
