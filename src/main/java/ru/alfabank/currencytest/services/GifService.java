package ru.alfabank.currencytest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import org.springframework.http.HttpHeaders;

/**
 * Предоставляет методы для получения GIF анимации.
 */
public interface GifService {
    HttpHeaders getRedirectHeader(LocalDate today, String base);

    String getRandomGif(String query);

    JsonNode getGifData(String query) throws JsonProcessingException;
}
