package ru.chaplyginma.httplogging.reactiveweb.decorator;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

    private final AtomicReference<String> requestBodyRef;

    public RequestLoggingDecorator(ServerHttpRequest delegate, AtomicReference<String> requestBodyRef) {
        super(delegate);
        this.requestBodyRef = requestBodyRef;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(dataBuffer -> {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            String requestBody = new String(content, StandardCharsets.UTF_8);
            requestBodyRef.set(requestBody);
            DataBufferUtils.release(dataBuffer);
        });
    }
}
