package ru.chaplyginma.httplogging.reactiveweb.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.chaplyginma.httplogging.reactiveweb.decorator.LoggingExchangeDecorator;
import ru.chaplyginma.httplogging.reactiveweb.logger.ReactiveWebHttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

@RequiredArgsConstructor
public class ReactiveWebHttpLoggingFilter implements WebFilter {

    private final HttpLoggingProperties httpLoggingProperties;
    private final ReactiveWebHttpLogger httpLogger;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        long startTime = System.currentTimeMillis();

        LoggingExchangeDecorator loggingExchangeDecorator = new LoggingExchangeDecorator(exchange);

        httpLogger.logRequest(loggingExchangeDecorator);

        return chain.filter(loggingExchangeDecorator)
                .doAfterTerminate(() -> httpLogger.logResponse(loggingExchangeDecorator, httpLoggingProperties, startTime));
    }
}