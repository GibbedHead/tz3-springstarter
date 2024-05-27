package ru.chaplyginma.httplogging.reactiveweb.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.chaplyginma.httplogging.reactiveweb.decorator.RequestLoggingDecorator;
import ru.chaplyginma.httplogging.reactiveweb.decorator.ResponseLoggingDecorator;
import ru.chaplyginma.httplogging.reactiveweb.logger.HttpLogger;
import ru.chaplyginma.httploggingproperties.properties.HttpLoggingProperties;

import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class ReactiveWebHttpLoggingFilter implements WebFilter {

    private final HttpLoggingProperties httpLoggingProperties;
    private final HttpLogger httpLogger;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!Boolean.TRUE.equals(httpLoggingProperties.getEnabled())) {
            return chain.filter(exchange);
        }

        long startTime = System.currentTimeMillis();

        AtomicReference<String> requestBodyRef = new AtomicReference<>("");
        AtomicReference<String> responseBodyRef = new AtomicReference<>("");

        ServerWebExchange mutatedExchange = exchange;

        if (httpLoggingProperties.getLogRequestBody()) {
            mutatedExchange = exchange.mutate()
                    .request(new RequestLoggingDecorator(exchange.getRequest(), requestBodyRef))
                    .build();
        }

        ServerWebExchange finalExchange = mutatedExchange;
        if (httpLoggingProperties.getLogResponseBody()) {
            finalExchange = finalExchange.mutate()
                    .response(new ResponseLoggingDecorator(exchange.getResponse(), responseBodyRef))
                    .build();
        }

        final ServerWebExchange finalExchangeCopy = finalExchange;
        return chain.filter(finalExchange)
                .then(Mono.fromRunnable(() -> {
                    httpLogger.logRequest(finalExchangeCopy, requestBodyRef);
                    httpLogger.logResponse(finalExchangeCopy, responseBodyRef, startTime);
                }));
    }
}
