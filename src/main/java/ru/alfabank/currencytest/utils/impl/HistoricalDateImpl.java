package ru.alfabank.currencytest.utils.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import org.springframework.stereotype.Component;
import ru.alfabank.currencytest.utils.HistoricalDate;

/**
 * Вспомогательный класс, содержит методы для вычисления нужного дня недели
 * и форматирования даты для формирования запроса о курсах валют.
 */
@Component
public class HistoricalDateImpl implements HistoricalDate {

    /**
     * Возвращает дату последнего рабочего дня относительно переданной даты.
     * Для выходных - пятница, для остальных дней недели - тот же день.
     *
     * @param date дата, относительно которой нужно вычислить рабочий день
     * @return LocalDate
     */
    @Override
    public LocalDate lastWeekday(LocalDate date) {
        if (isWeekend(date)) {
            return date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        }
        return date;
    }

    /**
     * Возвращает переданную дату рабочего дня минус количество дней deep.
     * Если текущий день - понедельник, увеличивает глубину запроса на два,
     * чтобы исключить выходные. Если результат приходится на выходной день,
     * возвращает предидущую пятницу.
     *
     * @param date дата, относительно которой нужно вычислить рабочий день
     * @param deep глубина запроса в архив
     * @return LocalDate
     */
    @Override
    public LocalDate dateMinusDeep(LocalDate date, int deep) {
        var result = minusDeep(date, deep);
        if (isWeekend(result)) {
            result = lastWeekday(result);
        }
        return result;
    }

    /**
     * Конвертирует дату в строку в формате pattern.
     * Для openexchangerates формат даты - yyyy-MM-dd
     *
     * @param date дата
     * @param pattern формат
     * @return String
     */
    @Override
    public String histDateFormatter(LocalDate date, String pattern) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    private boolean isWeekend(LocalDate date) {
        var dayOfWeek = date.getDayOfWeek();
        return DayOfWeek.SATURDAY.equals(dayOfWeek)
                || DayOfWeek.SUNDAY.equals(dayOfWeek);
    }

    private LocalDate minusDeep(LocalDate today, int deep) {
        var weekday = lastWeekday(today);
        if (DayOfWeek.MONDAY.equals(today.getDayOfWeek())) {
            return weekday.minusDays(deep + 2L);
        }
        return weekday.minusDays(deep);
    }
}
