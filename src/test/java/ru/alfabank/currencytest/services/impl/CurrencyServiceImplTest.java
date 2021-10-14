package ru.alfabank.currencytest.services.impl;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.model.ExRates;
import ru.alfabank.currencytest.model.Trend;
import ru.alfabank.currencytest.services.CurrencyService;
import ru.alfabank.currencytest.system.ConfigProperties;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class CurrencyServiceImplTest {

    @Autowired
    private ConfigProperties props;
    @Autowired
    private CurrencyService cs;

    @AfterEach
    void restWireMockScenarios() {
        RestTemplate restTemplate = new RestTemplate();
        var resetUrl = props.getOpenexchangerates().getAddr() + "/__admin/scenarios/reset";
        restTemplate.postForLocation(resetUrl, Void.class);
    }

    @Test
    void whenGetPositiveTrend_ThenGetNegativeTrend() {
        var positive = new Trend(true);
        var negative = new Trend(false);
        var posRequest = cs.getTrend(
                LocalDate.of(2021, 10, 14),
                props.getBase());
        var negRequest = cs.getTrend(
                LocalDate.of(2021, 10, 14),
                props.getBase());
        assertThat(posRequest).isEqualTo(positive);
        assertThat(negRequest).isEqualTo(negative);
    }

    @Test
    void whenRequestLastCurrency() {
        var expect = new ExRates(
                "Usage subject to terms: https://openexchangerates.org/terms",
                "https://openexchangerates.org/license",
                1634202000L,
                "USD",
                Collections.singletonMap("RUB", 71.714));
        var result = cs.getLastCurrency(props.getBase());
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void whenRequestHistoricalCurrency() {
        var expect = new ExRates(
                "Usage subject to terms: https://openexchangerates.org/terms",
                "https://openexchangerates.org/license",
                1634169583L,
                "USD",
                Collections.singletonMap("RUB", 72.0042));
        var result = cs.getHistoricCurrency(
                "2021-10-13",
                props.getBase());
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void whenGetErrorJson() {
        var expect = "403 FORBIDDEN \"Not allowed error message.\"";
        assertThatThrownBy(
                () -> cs.getLastCurrency("NONE")
        ).isInstanceOf(ResponseStatusException.class).hasMessage(expect);
    }
}