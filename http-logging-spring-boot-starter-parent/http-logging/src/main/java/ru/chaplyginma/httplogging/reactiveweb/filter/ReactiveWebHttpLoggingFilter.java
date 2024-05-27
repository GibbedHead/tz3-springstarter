package ru.chaplyginma.httplogging.reactiveweb.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.chaplyginma.httplogging.reactiveweb.decorator.LoggingExchangeDecorator;
import ru.chaplyginma.httplogging.reactiveweb.logger.HttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@RequiredArgsConstructor
@Slf4j
public class ReactiveWebHttpLoggingFilter implements WebFilter {

    private final HttpLoggingProperties httpLoggingProperties;
    private final HttpLogger httpLogger;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        long startTime = System.currentTimeMillis();

        LoggingExchangeDecorator loggingExchangeDecorator = new LoggingExchangeDecorator(exchange);

        httpLogger.logRequest(loggingExchangeDecorator);

        return chain.filter(loggingExchangeDecorator)
                .doOnError(error -> log.error("Error during request processing", error))
                .doOnSuccess((se) -> httpLogger.logResponse(loggingExchangeDecorator, httpLoggingProperties, startTime));
    }
}