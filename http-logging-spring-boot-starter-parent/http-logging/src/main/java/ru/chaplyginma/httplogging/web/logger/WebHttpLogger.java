package ru.chaplyginma.httplogging.web.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.chaplyginma.httplogging.logger.HttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class WebHttpLogger {
    private final HttpLogger httpLogger;

    public void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestUrl = requestUri + (queryString != null ? "?" + queryString : "");
        Map<String, String> requestHeaders = getRequestHeaders(request);

        httpLogger.logRequest(
                method,
                requestUrl,
                requestHeaders
        );
    }

    public void logResponse(
            HttpLoggingProperties httpLoggingProperties,
            ContentCachingResponseWrapper response,
            ContentCachingRequestWrapper request,
            long startTime
    ) throws IOException {
        long responseDuration = System.currentTimeMillis() - startTime;

        String responseStatus = String.valueOf(response.getStatus());

        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());

        response.copyBodyToResponse();

        Map<String, String> responseHeaders = getResponseHeaders(response);

        httpLogger.logResponse(
                httpLoggingProperties,
                requestBody,
                responseDuration,
                responseStatus,
                responseHeaders,
                responseBody
        );
    }

    private Map<String, String> getRequestHeaders(ContentCachingRequestWrapper request) {
        Map<String, String> requestHeaders = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(
                headerName -> requestHeaders.put(headerName, request.getHeader(headerName))
        );
        return requestHeaders;
    }

    private Map<String, String> getResponseHeaders(ContentCachingResponseWrapper response) {
        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaderNames().forEach(
                headerName -> responseHeaders.put(headerName, response.getHeader(headerName))
        );
        return responseHeaders;
    }
}
