package ru.chaplyginma.httplogging.reactiveweb.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.chaplyginma.httplogging.logger.HttpLogger;
import ru.chaplyginma.httplogging.reactiveweb.decorator.LoggingExchangeDecorator;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ReactiveWebHttpLogger {
    private final HttpLogger httpLogger;

    public void logRequest(LoggingExchangeDecorator exchange) {
        String method = exchange.getRequest().getMethod().toString();
        String requestUrl = exchange.getRequest().getURI().toString();
        Map<String, String> requestHeaders = exchange.getRequest().getHeaders().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().isEmpty() ? "" : entry.getValue().get(0)
                ));

        httpLogger.logRequest(
                method,
                requestUrl,
                requestHeaders
        );
    }

    public void logResponse(LoggingExchangeDecorator exchange, HttpLoggingProperties httpLoggingProperties, long startTime) {
        String requestBody = exchange.getRequest().getFullBody();

        long responseDuration = System.currentTimeMillis() - startTime;

        String responseStatus = Objects.requireNonNull(exchange.getResponse().getStatusCode()).toString();

        Map<String, String> responseHeaders = exchange.getResponse().getHeaders().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().isEmpty() ? "" : entry.getValue().get(0)
                ));

        String responseBody = exchange.getResponse().getFullBody();

        httpLogger.logResponse(
                httpLoggingProperties,
                requestBody,
                responseDuration,
                responseStatus,
                responseHeaders,
                responseBody
        );
    }
}
