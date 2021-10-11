package ru.alfabank.currencytest.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.alfabank.currencytest.model.ErrorMessage;

/**
 * Перехватывает исключение Feign и заменяет на стандартные.
 * Также пытается достать из ответа дополнительную информацию об ошибке.
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400 -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, getMessage(response).message());
            case 401 -> throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, getMessage(response).message());
            case 403 -> throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, getMessage(response).message());
            case 404 -> throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, response.reason());
            default -> throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, response.reason());
        }
    }

    /**
     * Пытается получить дополнительную информацию об ошибке.
     * Ищет поле description или message, если их нет, возвращает пустую строку.
     *
     * @param resp объект с информацией об исключении Feign
     * @return строка с текстом ошибки или пустая строка
     */
    private ErrorMessage getMessage(Response resp) {
        var mapper = new ObjectMapper();
        var errorMessage = new ErrorMessage("");
        try {
            var errorResp = mapper.readTree(resp.body().asInputStream());
            var message = errorResp.get("description").asText();
            message = Objects.requireNonNullElse(message, errorResp.get("message").asText());
            message = Objects.requireNonNullElse(message, "");
            errorMessage = new ErrorMessage(message);
        } catch (IOException e) {
            log.error(
                    "Can't parsing error response from external service: {}",
                    e.getMessage());
        }
        return errorMessage;
    }
}
