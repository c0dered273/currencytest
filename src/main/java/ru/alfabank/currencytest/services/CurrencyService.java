package ru.alfabank.currencytest.services;

import java.time.LocalDate;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.model.Trend;

/**
 * Предоставляет методы для запроса и обработки курсов валют.
 */
public interface CurrencyService {
    Trend getTrend(LocalDate today, String base);

    ExRates getLastCurrency(String baseCurrency);

    ExRates getHistoricCurrency(String date, String baseCurrency);

}
