package ru.alfabank.currencytest.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.clients.GiphyClient;
import ru.alfabank.currencytest.services.CurrencyService;
import ru.alfabank.currencytest.services.GifService;
import ru.alfabank.currencytest.system.ConfigProperties;

/**
 * Обеспечивает работу со ссылками на GIF анимацию.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {

    private final ConfigProperties props;
    private final CurrencyService currencyService;
    private final GiphyClient giphyClient;

    /**
     * Возвращает http заголовок со ссылкой на GIF, полученной в результате сравнения курса валют.
     * Выбирается случайная ссылка из поисковой выдачи giphy.com. Поисковый запрос формируется
     * по результату сравнения курсов валют.
     *
     * @param base код базовой валюты, относительно которой берется курс
     * @return HttpHeaders
     */
    @Override
    public HttpHeaders getRedirectHeader(LocalDate today, String base) {
        var headers = new HttpHeaders();
        var trend = currencyService.getTrend(today, base);
        try {
            if (trend.positive()) {
                headers.setLocation(new URI(getRandomGif("rich")));
            } else {
                headers.setLocation(new URI(getRandomGif("broke")));
            }
        } catch (URISyntaxException e) {
            log.error("Can`t parse link to GIF: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Can`t parse link to GIF");
        }
        return headers;
    }

    /**
     * Возвращает ссылку на случайную GIF из поисковой выдачи giphy.com.
     *
     * @param query строка поискового запроса
     * @return ссылка на GIF
     */
    @Override public String getRandomGif(String query) {
        String result;
        try {
            var gifData = getGifData(query);
            result = getRandomLink(parseRespForLinks(gifData));
        } catch (JsonProcessingException e) {
            log.error("Error json processing of Gihpy.com search response: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error json processing of Gihpy.com");
        }
        return result;
    }

    /**
     * Возвращает сырой JSON ответ на поисковый запрос giphy.com.
     *
     * @param query строка поискового запроса
     * @return JsonNode
     * @throws JsonProcessingException в случае ошибки структуры JSON
     */
    @Override public JsonNode getGifData(String query) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.readTree(
                        giphyClient.search(
                                props.getGiphy().getApiKey(),
                                query,
                                props.getGiphy().getSearchLimit()))
                .get("data");
    }

    /**
     * Ищет строки со ссылками на GIF в структуре ответа на поисковый запрос.
     * Если в ответе сервера ссылок нет, возвращает пустой список.
     *
     * @param jsonNode сырой JSON ответ на поисковый запрос
     * @return список строк со ссылками
     */
    private List<String> parseRespForLinks(JsonNode jsonNode) {
        List<String> result = new ArrayList<>();
        if (jsonNode == null) {
            return result;
        }
        for (var iter = jsonNode.elements(); iter.hasNext();) {
            var gifData = iter.next();
            var imageUrl = gifData
                    .get("images")
                    .get("original")
                    .get("url").asText();
            if (imageUrl == null) {
                imageUrl = "";
            }
            result.add(imageUrl);
        }
        return result;
    }

    private String getRandomLink(List<String> links) {
        return links.get(ThreadLocalRandom.current().nextInt(links.size() - 1));
    }

}
