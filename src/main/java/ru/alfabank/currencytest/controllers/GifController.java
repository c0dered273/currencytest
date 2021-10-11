package ru.alfabank.currencytest.controllers;

import java.util.Optional;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/currency")
public class GifController {

    private final CurrencyService currencyService;
    private final GifService gifService;

    @GetMapping("/status")
    public ResponseEntity<Object> currencyStatus(
            @RequestParam(required = false) String base) {
        var headers = gifService.getRedirectHeader(base);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/latest")
    public ExRates latestStatus(
            @RequestParam(required = false) String base) {
        return currencyService.getLastCurrency(Optional.ofNullable(base));
    }

    @GetMapping("/historical/{date}")
    public ExRates historicalStatus(
            @PathVariable(name = "date") String date,
            @RequestParam(required = false) String base) {
        return currencyService.getHistoricCurrency(date, Optional.ofNullable(base));
    }
}
