package ru.alfabank.currencytest.controllers;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.services.CurrencyService;
import ru.alfabank.currencytest.services.GifService;

/**
 * Возвращает информацию о курсе валюты в виде JSON
 * или перенаправляет клиента на адрес GIF картинки.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/currency")
public class GifController {

    private final CurrencyService currencyService;
    private final GifService gifService;

    /**
     * Перенаправляет клиента на GIF. Тематика картинки зависит от динамики курса валюты,
     * указанной в настройках как app.quote.
     * Если курс повысился, возвращается GIF по поисковому запросу "rich", если понизился -
     * по поисковому запросу "broke".
     *
     * @param base код базовой валюты, относительно которой берется курс
     * @return redirect to url
     */
    @GetMapping("/status")
    public ResponseEntity<Object> currencyStatus(
            @RequestParam(required = false) String base) {
        var headers = gifService.getRedirectHeader(LocalDate.now(), base);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * Возвращает данные о курсе валюты на сегодня.
     *
     * @param base код базовой валюты, относительно которой берется курс
     * @return JSON
     */
    @GetMapping("/latest")
    public ExRates latestStatus(
            @RequestParam(required = false) String base) {
        return currencyService.getLastCurrency(base);
    }

    /**
     * Возвращает архивный курс валюты.
     *
     * @param date дата, за которую нужен архив (yyyy-MM-dd)
     * @param base код базовой валюты, относительно которой берется курс
     * @return JSON
     */
    @GetMapping("/historical/{date}")
    public ExRates historicalStatus(
            @PathVariable(name = "date") String date,
            @RequestParam(required = false) String base) {
        return currencyService.getHistoricCurrency(date, base);
    }
}
