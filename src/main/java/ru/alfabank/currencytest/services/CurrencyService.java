package ru.alfabank.currencytest.services;

import static ru.alfabank.currencytest.utils.HistoricalDate.histDateFormatter;
import static ru.alfabank.currencytest.utils.HistoricalDate.lastWeekday;
import static ru.alfabank.currencytest.utils.HistoricalDate.weekdayMinusDeep;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alfabank.currencytest.clients.ExchangeRatesClient;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.model.Trend;
import ru.alfabank.currencytest.system.ConfigProperties;

/**
 * Предоставляет данные о курсах валют и результатах их сравнения.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {

    private final ConfigProperties props;
    private final ExchangeRatesClient exchangeRatesClient;

    /**
     * Возвращает результат сравнения курса валюты за сегодня и из архива.
     * Глубина архива задается параметром app.deep. Результат Trend.positive = true, возвращается
     * если курс base/quote понизился относительно архивного значения,
     * т.е. валюта app.quote стала дороже.
     * Архивное значение берется за предыдущий рабочий день, если запрос приходится
     * на выходные дни или понедельник, возвращается значение за предыдущую пятницу.
     *
     * @param base код базовой валюты, относительно которой берется курс
     * @return Trend или исключение ResponseStatusException
     */
    public Trend getTrend(Optional<String> base) {
        var lastRates = getLastCurrency(base);
        var historicalRates = getHistoricCurrency(
                getHistoricalDate("yyyy-MM-dd"), base);
        return compareRates(lastRates, historicalRates);
    }

    /**
     * Возвращает JSON с курсами валют за сегодня.
     *
     * @param baseCurrency код базовой валюты, относительно которой берется курс
     * @return ExRates или исключение ResponseStatusException
     */
    public ExRates getLastCurrency(Optional<String> baseCurrency) {
        var base = baseCurrency.orElse(props.getBase());
        return exchangeRatesClient.getLatest(props.getOpenexchangerates().getAppId(),
                base,
                props.getQuote());
    }

    /**
     * Возвращает JSON с курсами валют из архива за указанную дату.
     *
     * @param date дата, за которую нужен архив (yyyy-MM-dd)
     * @param baseCurrency код базовой валюты, относительно которой берется курс
     * @return ExRates или исключение ResponseStatusException
     */
    public ExRates getHistoricCurrency(String date, Optional<String> baseCurrency) {
        var base = baseCurrency.orElse(props.getBase());
        return exchangeRatesClient.getHistoric(date,
                props.getOpenexchangerates().getAppId(),
                base,
                props.getQuote());
    }

    /**
     * Возвращает дату рабочего дня отстоящего от сегодняшнего момента на значение app.deep.
     * Дата форматируется согласно переданному паттерну.
     * Используется для запроса валюты за предыдущий рабочий день.
     *
     * @param pattern паттерн для форматирования даты, для openexchangerates - yyyy-MM-dd
     * @return String
     */
    public String getHistoricalDate(String pattern) {
        var histDay = weekdayMinusDeep(
                lastWeekday(LocalDate.now()), props.getDeep());
        return histDateFormatter(histDay, pattern);
    }

    private Trend compareRates(ExRates last, ExRates hist) {
        var lastValue = last.rates().get(props.getQuote());
        var histValue = hist.rates().get(props.getQuote());
        return new Trend(histValue.compareTo(lastValue) > 0);
    }
}
