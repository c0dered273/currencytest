package ru.alfabank.currencytest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Получает от giphy.com результат поискового запроса по ключевому слову.
 */
@FeignClient(name = "giphyClient", url = "${app.giphy.addr}")
public interface GiphyClient {

    /**
     * Возвращает результат запроса на поиск GIF картинок.
     *
     * @param apiKey токен для доступа к API
     * @param query строка поискового запроса
     * @param limit лимит выдачи результатов поиска (для бесплатной версии максимум 50)
     * @return JSON строку с результатом запроса или выбрасывает исключение FeignException
     */
    @GetMapping("/search")
    String search(@RequestParam(name = "api_key") String apiKey,
                  @RequestParam(name = "q") String query,
                  @RequestParam(name = "limit") String limit);

}
