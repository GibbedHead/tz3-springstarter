package ru.chaplyginma.httplogging.reactiveweb.logger;

import lombok.extern.slf4j.Slf4j;
import ru.chaplyginma.httplogging.reactiveweb.decorator.LoggingExchangeDecorator;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class HttpLogger {

    public void logRequest(LoggingExchangeDecorator exchange) {
        String method = exchange.getRequest().getMethod().toString();
        String requestUrl = exchange.getRequest().getURI().toString();
        String requestHeaders = exchange.getRequest().getHeaders().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(value -> String.format("%s: %s", entry.getKey(), value)))
                .collect(Collectors.joining("\n\t\t"));
        String logMessageTemplate = """
                                
                Request:
                \tMethod = {}
                \tRequest URL = {}
                \tRequest Headers =
                \t\t{}
                """;

        log.info(
                logMessageTemplate,
                method,
                requestUrl,
                requestHeaders
        );
    }

    public void logResponse(LoggingExchangeDecorator exchange, HttpLoggingProperties httpLoggingProperties, long startTime) {
        String requestBody = exchange.getRequest().getFullBody();

        long responseDuration = System.currentTimeMillis() - startTime;

        String responseStatus = Objects.requireNonNull(exchange.getResponse().getStatusCode()).toString();

        String responseHeaders = exchange.getResponse().getHeaders().entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(value -> String.format("%s: %s", entry.getKey(), value)))
                .collect(Collectors.joining("\n\t\t"));

        String responseBody = exchange.getResponse().getFullBody();

        String requestBodyLogMessage = String.format("""
                                                
                        Request:
                        \tRequest body =
                        %s
                        """,
                requestBody
        );

        String responseBodyLogMessage = String.format("""
                        Response body =
                        %s
                        """,
                responseBody
        );

        String logMessage = String.format(
                """
                                                
                        Response:
                        \tResponse Status = %s
                        \tResponse Headers =
                        \t\t%s
                        \tRequest Duration = %dms
                        """,
                responseStatus,
                responseHeaders,
                responseDuration
        );
        logMessage = (httpLoggingProperties.getLogRequestBody() && !requestBody.isBlank() ? requestBodyLogMessage : "")
                + logMessage
                + (httpLoggingProperties.getLogResponseBody() && !responseBody.isBlank() ? responseBodyLogMessage : "");

        log.info(logMessage);
    }
}
