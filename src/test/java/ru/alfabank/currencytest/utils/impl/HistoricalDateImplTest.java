package ru.alfabank.currencytest.utils.impl;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;


class HistoricalDateImplTest {

    @Test
    void whenWeekday_thenReturnSameDay() {
        var hd = new HistoricalDateImpl();
        var weekday = LocalDate.of(2021, 10, 12);
        assertThat(hd.lastWeekday(weekday)).isEqualTo(weekday);
    }

    @Test
    void whenWeekend_thenReturnFriday() {
        var hd = new HistoricalDateImpl();
        var today = LocalDate.of(2021, 10, 9);
        var expect = LocalDate.of(2021, 10, 8);
        assertThat(hd.lastWeekday(today)).isEqualTo(expect);
    }

    @Test
    void whenWeekday_ThenReturnMinusOneDay() {
        var hd = new HistoricalDateImpl();
        var today = LocalDate.of(2021, 10, 5);
        var expect = LocalDate.of(2021, 10, 4);
        assertThat(hd.dateMinusDeep(today, 1)).isEqualTo(expect);
    }

    @Test
    void whenWeekend_AndMinusOne_ThenReturnThursday() {
        var hd = new HistoricalDateImpl();
        var today = LocalDate.of(2021, 10, 10);
        var expect = LocalDate.of(2021, 10, 7);
        assertThat(hd.dateMinusDeep(today, 1)).isEqualTo(expect);
    }

    @Test
    void whenMonday_AndMinusOne_ThenReturnFriday() {
        var hd = new HistoricalDateImpl();
        var today = LocalDate.of(2021, 9, 20);
        var expect = LocalDate.of(2021, 9, 17);
        assertThat(hd.dateMinusDeep(today, 1)).isEqualTo(expect);
    }

    @Test
    void whenResultIsWeekend_ThenReturnFriday() {
        var hd = new HistoricalDateImpl();
        var today = LocalDate.of(2021, 9, 23);
        var expect = LocalDate.of(2021, 9, 17);
        assertThat(hd.dateMinusDeep(today, 4)).isEqualTo(expect);
    }
}