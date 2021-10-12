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
     * @param localDate дата, относительно которой нужно вычислить рабочий день
     * @return LocalDate
     */
    @Override
    public LocalDate lastWeekday(LocalDate localDate) {
        var dayOfWeek = localDate.getDayOfWeek();
        if (DayOfWeek.SATURDAY.equals(dayOfWeek)
                || DayOfWeek.SUNDAY.equals(dayOfWeek)) {
            return localDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
        }
        return localDate;
    }

    /**
     * Возвращает переданную дату рабочего дня минус количество дней deep.
     * Если текущий день - понедельник, увеличивает глубину запроса на два,
     * чтобы исключить выходные.
     *
     * @param localDate дата, относительно которой нужно вычислить рабочий день
     * @param deep глубина запроса в архив
     * @return LocalDate
     */
    @Override
    public LocalDate weekdayMinusDeep(LocalDate localDate, int deep) {
        var dayOfWeek = LocalDate.now().getDayOfWeek();
        if (DayOfWeek.MONDAY.equals(dayOfWeek)) {
            return localDate.minusDays(deep + 2L);
        }
        return localDate.minusDays(deep);
    }

    /**
     * Конвертирует дату в строку в формате pattern.
     * Для openexchangerates формат даты - yyyy-MM-dd
     *
     * @param localDate дата
     * @param pattern формат
     * @return String
     */
    @Override
    public String histDateFormatter(LocalDate localDate, String pattern) {
        var formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }
}
