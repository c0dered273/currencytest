package ru.alfabank.currencytest.services.impl;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.services.GifService;
import ru.alfabank.currencytest.system.ConfigProperties;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
class GifServiceImplTest {

    @Autowired
    private ConfigProperties props;
    @Autowired
    private GifService gs;

    @AfterEach
    void restWireMockScenarios() {
        RestTemplate restTemplate = new RestTemplate();
        var resetUrl = props.getOpenexchangerates().getAddr() + "/__admin/scenarios/reset";
        restTemplate.postForLocation(resetUrl, Void.class);
    }

    @Test
    void whenGetPositiveRedirect_thenGetNegativeRedirect() {
        var expectPos = URI.create("https://link-to-rich-gif");
        var expectNeg = URI.create("https://link-to-broke-gif");
        var positive = gs.getRedirectHeader(
                LocalDate.of(2021, 10, 14),
                props.getBase()).getLocation();
        var negative = gs.getRedirectHeader(
                LocalDate.of(2021, 10, 14),
                props.getBase()).getLocation();
        assertThat(positive).isEqualTo(expectPos);
        assertThat(negative).isEqualTo(expectNeg);
    }

    @Test
    void whenGetGifData() throws JsonProcessingException {
        var randomData = gs.getGifData("random");
        var expect = "ShRrI47qUtKf9ynTvz";
        var result = randomData.get(49).get("id").asText();
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void whenGetOneLinkInData() throws JsonProcessingException {
        var randomData = gs.getGifData("one");
        var expect = "https://one-link";
        var result = randomData.get(0).get("images").get("original").get("url").asText();
        assertThat(result).isEqualTo(expect);
    }

    @Test
    void whenGetEmptyData() {
        assertThatThrownBy(
                () -> gs.getRandomGif("none")
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void whenGetRandomGif() throws JsonProcessingException {
        List<String> gifList = new ArrayList<>();
        var randomData = gs.getGifData("random");
        for (var iter = randomData.elements(); iter.hasNext();) {
            var gifData = iter.next();
            var imageUrl = gifData
                    .get("images")
                    .get("original")
                    .get("url").asText();
            if (imageUrl == null) {
                imageUrl = "";
            }
            gifList.add(imageUrl);
        }
        var result = gs.getRandomGif("random");
        assertThat(gifList.contains(result)).isTrue();
    }
}