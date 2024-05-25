package ru.chaplyginma.httplogging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response, startTime);
        }
    }

    private void logRequest(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestUrl = requestUri + (queryString != null ? "?" + queryString : "");
        String requestHeaders = getRequestHeaders(request);
        String requestBody = getRequestBody(request);
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Request\n");
        logMessage.append("Method: ").append(method).append("\n");
        logMessage.append("Request URL: ").append(requestUrl).append("\n");
        logMessage.append("Request Headers:\n\t").append(requestHeaders).append("\n");
        if (!requestBody.isEmpty() && httpLoggingProperties.getLogRequestBody()) {
            logMessage.append("Request Body:\n").append(requestBody);
        }
        log.info(logMessage.toString());
    }

    private void logResponse(HttpServletResponse response, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        int status = response.getStatus();
        String responseHeaders = getResponseHeaders(response);
        log.info(
                "Response Status: {}; Response Headers: {}; Response Body: {}; Request Duration: {}ms",
                status,
                responseHeaders,
                "",
                duration
        );
    }

    private String getRequestHeaders(HttpServletRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(
                headerName -> requestHeaders.put(headerName, request.getHeader(headerName))
        );
        return requestHeaders.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n\t"));
    }

    private String getResponseHeaders(HttpServletResponse response) {
        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaderNames().forEach(
                headerName -> responseHeaders.put(headerName, response.getHeader(headerName))
        );
        return responseHeaders.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n\t"));
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }
        return requestBody.toString();
    }
}
