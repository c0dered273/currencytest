package ru.alfabank.currencytest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfabank.currencytest.model.ExRates;

@FeignClient(name = "exchangeRates", url = "https://openexchangerates.org/api/")
public interface ExchangeRatesClient {

    @GetMapping("/latest.json")
    ExRates getLatest(@RequestParam(name = "app_id") String appId,
                      @RequestParam(name = "base") String base,
                      @RequestParam(name = "symbols") String symbols);

    @GetMapping("/historical/{date}.json")
    ExRates getHistoric(@PathVariable(name = "date") String date,
                        @RequestParam(name = "app_id") String appId,
                        @RequestParam(name = "base") String base,
                        @RequestParam(name = "symbols") String symbols);

}
