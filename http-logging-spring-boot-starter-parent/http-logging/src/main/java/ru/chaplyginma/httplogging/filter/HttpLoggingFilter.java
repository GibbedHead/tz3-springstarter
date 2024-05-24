package ru.chaplyginma.httplogging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class HttpLoggingFilter extends OncePerRequestFilter {
    private final HttpLoggingProperties httpLoggingProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        if (httpLoggingProperties.getLogRequestBody()) {
            log.info("Log request body");
        }
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response, startTime);
        }
    }

    private void logRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestUrl = requestUri + (queryString != null ? "?" + queryString : "");
        String requestHeaders = getRequestHeaders(request).entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
        log.info(
                "Request Method: {}; Request URL: {}; Request Headers: {}",
                method,
                requestUrl,
                requestHeaders
        );
    }

    private void logResponse(HttpServletResponse response, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        int status = response.getStatus();
        String responseHeaders = getResponseHeaders(response).entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
        log.info(
                "Response Status: {}; Response Headers: {}; Response Body: {}; Request Duration: {}ms",
                status,
                responseHeaders,
                "",
                duration
        );
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> requestHeaders.put(headerName, request.getHeader(headerName)));
        return requestHeaders;
    }

    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaderNames().forEach(headerName -> responseHeaders.put(headerName, response.getHeader(headerName)));
        return responseHeaders;
    }
}
