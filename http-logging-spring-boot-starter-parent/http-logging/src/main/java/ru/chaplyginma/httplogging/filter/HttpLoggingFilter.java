package ru.chaplyginma.httplogging.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class HttpLoggingFilter extends OncePerRequestFilter {
    private final HttpLoggingProperties httpLoggingProperties;

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        wrappedRequest.getParameterMap();

        try {
            logRequest(wrapRequest(wrappedRequest));
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logResponse(wrapResponse(wrappedResponse), wrappedRequest, startTime);
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String queryString = request.getQueryString();
        String requestUrl = requestUri + (queryString != null ? "?" + queryString : "");
        String requestHeaders = getRequestHeaders(request);

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

    private void logResponse(
            ContentCachingResponseWrapper response,
            ContentCachingRequestWrapper request,
            long startTime
    ) throws IOException {
        long responseDuration = System.currentTimeMillis() - startTime;
        int responseStatus = response.getStatus();

        String requestBody = new String(request.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());

        response.copyBodyToResponse();

        String responseHeaders = getResponseHeaders(response);

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

    private String getRequestHeaders(ContentCachingRequestWrapper request) {
        Map<String, String> requestHeaders = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(
                headerName -> requestHeaders.put(headerName, request.getHeader(headerName))
        );
        return requestHeaders.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n\t\t"));
    }

    private String getResponseHeaders(ContentCachingResponseWrapper response) {
        Map<String, String> responseHeaders = new HashMap<>();
        response.getHeaderNames().forEach(
                headerName -> responseHeaders.put(headerName, response.getHeader(headerName))
        );
        return responseHeaders.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n\t\t"));
    }
}
