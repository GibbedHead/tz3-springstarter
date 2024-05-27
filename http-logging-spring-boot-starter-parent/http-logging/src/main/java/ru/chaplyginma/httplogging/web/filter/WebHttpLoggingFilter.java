package ru.chaplyginma.httplogging.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import ru.chaplyginma.httplogging.web.logger.WebHttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.io.IOException;

@RequiredArgsConstructor
public class WebHttpLoggingFilter extends OncePerRequestFilter {
    private final HttpLoggingProperties httpLoggingProperties;
    private final WebHttpLogger webHttpLogger;

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
        webHttpLogger.logRequest(request);
    }

    private void logResponse(
            ContentCachingResponseWrapper response,
            ContentCachingRequestWrapper request,
            long startTime
    ) throws IOException {
        webHttpLogger.logResponse(
                httpLoggingProperties,
                response,
                request,
                startTime
        );
    }


}
