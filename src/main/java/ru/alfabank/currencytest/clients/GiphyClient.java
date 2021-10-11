package ru.alfabank.currencytest.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphyClient", url = "${app.giphy.addr}")
public interface GiphyClient {

    @GetMapping("/search")
    String search(@RequestParam(name = "api_key") String apiKey,
                  @RequestParam(name = "q") String query,
                  @RequestParam(name = "limit") String limit);

}
