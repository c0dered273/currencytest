package ru.alfabank.currencytest.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.model.ExRatesErrorDto;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, parseResp(response).description());
            case 401 -> throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, parseResp(response).description());
            case 403 -> throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, parseResp(response).description());
            case 404 -> throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, response.reason());
            default -> throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, response.reason());
        }
    }

    private ExRatesErrorDto parseResp(Response resp) {
        var defaultMsg = "Can't parse openexchangerates.org response";
        var mapper = new ObjectMapper();
        var exRatesError = new ExRatesErrorDto(true, 999, defaultMsg, defaultMsg);
        try {
            exRatesError = mapper.readValue(resp.body().asInputStream(), ExRatesErrorDto.class);
        } catch (IOException e) {
            log.error(
                    "Can't parsing error response from openexchangerates.org: {}",
                    e.getMessage());
        }
        return exRatesError;
    }
}
