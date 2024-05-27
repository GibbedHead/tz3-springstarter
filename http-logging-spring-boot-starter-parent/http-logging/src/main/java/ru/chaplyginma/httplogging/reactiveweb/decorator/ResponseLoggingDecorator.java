package ru.chaplyginma.httplogging.reactiveweb.decorator;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class ResponseLoggingDecorator extends ServerHttpResponseDecorator {

    private final AtomicReference<String> responseBodyRef;

    public ResponseLoggingDecorator(ServerHttpResponse delegate, AtomicReference<String> responseBodyRef) {
        super(delegate);
        this.responseBodyRef = responseBodyRef;
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        Flux<DataBuffer> fluxBody = Flux.from(body);
        return super.writeWith(fluxBody.map(dataBuffer -> {
            byte[] content = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(content);
            String responseBody = new String(content, StandardCharsets.UTF_8);
            responseBodyRef.set(responseBody);
            return bufferFactory().wrap(content);
        }));
    }
}
