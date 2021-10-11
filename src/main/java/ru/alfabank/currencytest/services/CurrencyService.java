package ru.alfabank.currencytest.services;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alfabank.currencytest.clients.ExchangeRatesClient;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.model.Trend;
import ru.alfabank.currencytest.system.ConfigProperties;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    private final ConfigProperties props;
    private final ExchangeRatesClient exchangeRatesClient;

    public Trend getTrend(Optional<String> base) {
        var lastRates = getLastCurrency(base);
        var historicalRates = getHistoricCurrency(histDate(LocalDateTime.now()), base);
        return compareRates(lastRates, historicalRates);
    }

    public ExRates getLastCurrency(Optional<String> baseCurrency) {
        var base = baseCurrency.orElse(props.getBase());
        return exchangeRatesClient.getLatest(props.getOpenexchangerates().getAppId(),
                base,
                props.getQuote());
    }

    public ExRates getHistoricCurrency(String date, Optional<String> baseCurrency) {
        var base = baseCurrency.orElse(props.getBase());
        return exchangeRatesClient.getHistoric(date,
                props.getOpenexchangerates().getAppId(),
                base,
                props.getQuote());
    }

    private LocalDateTime lastWeekday(LocalDateTime dateTime) {
        var dayOfWeek = dateTime.getDayOfWeek();
        if (DayOfWeek.SATURDAY.equals(dayOfWeek)
                || DayOfWeek.SUNDAY.equals(dayOfWeek)
                || DayOfWeek.MONDAY.equals(dayOfWeek)) {
            return LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        }
        return LocalDateTime.now();
    }

    private String histDate(LocalDateTime localDateTime) {
        var histDate = lastWeekday(localDateTime).minusDays(props.getDeep());
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return histDate.format(formatter);
    }

    private Trend compareRates(ExRates last, ExRates hist) {
        var lastValue = last.rates().get(props.getQuote());
        var histValue = hist.rates().get(props.getQuote());
        return new Trend(histValue.compareTo(lastValue) > 0);
    }
}
