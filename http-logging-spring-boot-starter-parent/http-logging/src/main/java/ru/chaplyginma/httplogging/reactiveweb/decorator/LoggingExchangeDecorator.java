package ru.chaplyginma.httplogging.reactiveweb.decorator;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

public class LoggingExchangeDecorator extends ServerWebExchangeDecorator {
    private final RequestLoggingDecorator requestLoggingDecorator;
    private final ResponseLoggingDecorator responseLoggingDecorator;

    public LoggingExchangeDecorator(ServerWebExchange delegate) {
        super(delegate);
        requestLoggingDecorator = new RequestLoggingDecorator(delegate.getRequest());
        responseLoggingDecorator = new ResponseLoggingDecorator(delegate.getResponse());
    }

    @Override
    public RequestLoggingDecorator getRequest() {
        return requestLoggingDecorator;
    }

    @Override
    public ResponseLoggingDecorator getResponse() {
        return responseLoggingDecorator;
    }
}
