package ru.alfabank.currencytest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfabank.currencytest.model.ExRates;

/**
 * Получает от openexchangerates.org текущий и архивный курс валюты.
 */
@FeignClient(name = "exchangeRates", url = "${app.openexchangerates.addr}")
public interface ExchangeRatesClient {

    /**
     * Возвращает курс валюты на сегодня.
     *
     * @param appId токен для доступа к API
     * @param base код базовой валюты, относительно которой берется курс
     * @param symbols код валюты, курс которой нам нужен
     * @return ExRates или выбрасывает исключение FeignException
     */
    @GetMapping("/latest.json")
    ExRates getLatest(@RequestParam(name = "app_id") String appId,
                      @RequestParam(name = "base") String base,
                      @RequestParam(name = "symbols") String symbols);

    /**
     * Возвращает архивный курс валюты.
     *
     * @param date дата, за которую нужен архив (yyyy-MM-dd)
     * @param appId токен для доступа к API
     * @param base код базовой валюты, относительно которой берется курс
     * @param symbols код валюты, курс которой нам нужен
     * @return ExRates или выбрасывает исключение FeignException
     */
    @GetMapping("/historical/{date}.json")
    ExRates getHistoric(@PathVariable(name = "date") String date,
                        @RequestParam(name = "app_id") String appId,
                        @RequestParam(name = "base") String base,
                        @RequestParam(name = "symbols") String symbols);

}
