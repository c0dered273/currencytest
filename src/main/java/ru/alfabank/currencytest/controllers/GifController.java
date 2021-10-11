package ru.alfabank.currencytest.controllers;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.services.CurrencyService;
import ru.alfabank.currencytest.services.GifService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/currency")
public class GifController {

    private final CurrencyService cs;
    private final GifService gs;

    @GetMapping("/status")
    public boolean currencyStatus(
            @RequestParam(required = false) String base) {
        gs.getGifLinksList("rich");
        return cs.getTrend(Optional.ofNullable(base)).positive();
    }

    @GetMapping("/latest")
    public ExRates latestStatus(
            @RequestParam(required = false) String base) {
        return cs.getLastCurrency(Optional.ofNullable(base));
    }

    @GetMapping("/historical/{date}")
    public ExRates historicalStatus(
            @PathVariable(name = "date") String date,
            @RequestParam(required = false) String base) {
        return cs.getHistoricCurrency(date, Optional.ofNullable(base));
    }
}
