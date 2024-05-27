package ru.chaplyginma.httplogging.logger;

import lombok.extern.slf4j.Slf4j;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class HttpLogger {

    public void logRequest(
            String requestMethod,
            String requestUrl,
            Map<String, String> requestHeaders
    ) {
        String logMessageTemplate = """
                
                Request:
                \tMethod = {}
                \tRequest URL = {}
                \tRequest Headers =
                \t\t{}
                """;
        log.info(
                logMessageTemplate,
                requestMethod,
                requestUrl,
                requestHeaders.entrySet().stream()
                        .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                        .collect(Collectors.joining("\n\t\t"))
        );
    }

    public void logResponse(
            HttpLoggingProperties httpLoggingProperties,
            String requestBody,
            long responseDuration,
            String responseStatus,
            Map<String, String> responseHeaders,
            String responseBody
    ) {
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

        String logMessage = String.format("""
                        
                        Response:
                        \tResponse Status = %s
                        \tResponse Headers =
                        \t\t%s
                        \tRequest Duration = %dms
                        """,
                responseStatus,
                responseHeaders.entrySet().stream()
                        .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                        .collect(Collectors.joining("\n\t\t")),
                responseDuration
        );
        logMessage = (httpLoggingProperties.getLogRequestBody() && !requestBody.isBlank() ? requestBodyLogMessage : "")
                + logMessage
                + (httpLoggingProperties.getLogResponseBody() && !responseBody.isBlank() ? responseBodyLogMessage : "");

        log.info(logMessage);
    }
}
