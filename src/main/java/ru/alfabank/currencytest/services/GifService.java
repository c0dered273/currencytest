package ru.alfabank.currencytest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;

/**
 * Предоставляет методы для получения GIF анимации.
 */
public interface GifService {
    HttpHeaders getRedirectHeader(String base);

    String getRandomGif(String query);

    JsonNode getGifData(String query) throws JsonProcessingException;
}
