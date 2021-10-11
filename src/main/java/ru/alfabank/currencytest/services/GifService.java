package ru.alfabank.currencytest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.clients.GiphyClient;
import ru.alfabank.currencytest.system.ConfigProperties;

@Service
@Slf4j
@RequiredArgsConstructor
public class GifService {

    private final ConfigProperties props;
    private final CurrencyService currencyService;
    private final GiphyClient giphyClient;

    public HttpHeaders getRedirectHeader(String base) {
        var headers = new HttpHeaders();
        var trend = currencyService.getTrend(Optional.ofNullable(base));
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

    public String getRandomGif(String query) {
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

    public JsonNode getGifData(String query) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        return mapper.readTree(
                        giphyClient.search(
                                props.getGiphy().getApiKey(),
                                query,
                                props.getGiphy().getSearchLimit()))
                .get("data");
    }

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
