package ru.alfabank.currencytest.utils;

import java.time.LocalDate;

/**
 * Содержит методы для вычисления нужного дня недели
 * форматирования даты для формирования запроса о курсах валют.
 */
public interface HistoricalDate {
    LocalDate lastWeekday(LocalDate localDate);

    LocalDate dateMinusDeep(LocalDate localDate, int deep);

    String histDateFormatter(LocalDate localDate, String pattern);
}
