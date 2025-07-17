package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.edme.exception.EmptyResponseException;
import ru.edme.exception.ServerErrorException;

@Service
@RequiredArgsConstructor
public class RemoteRestClient {

    private final RestTemplate restTemplate;

    /**
     * При ошибках ServerErrorException и EmptyResponseException
     * повторяем до 3 раз с задержкой.
     */
    @Retryable(
            value = { ServerErrorException.class, EmptyResponseException.class },
            maxAttemptsExpression = "${remote.retry.max-attempts}",
            backoff = @Backoff(delayExpression = "${remote.retry.backoff}")
    )
    public String callExternal(String url) {
        var response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is5xxServerError()) {
            throw new ServerErrorException("5xx from " + url);
        }
        String body = response.getBody();
        if (body == null || body.isBlank()) {
            throw new EmptyResponseException("Empty body from " + url);
        }
        return body;
    }
}
