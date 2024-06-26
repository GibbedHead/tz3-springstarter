package ru.chaplyginma.httplogging.reactiveweb.decorator;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RequestLoggingDecorator extends ServerHttpRequestDecorator {

    private final StringBuilder body = new StringBuilder();

    public RequestLoggingDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    public Flux<DataBuffer> getBody() {
        return super.getBody().doOnNext(this::capture);
    }

    private void capture(DataBuffer buffer) {
        ByteBuffer copyBuffer = ByteBuffer.allocate(buffer.readableByteCount());
        buffer.toByteBuffer(copyBuffer);
        this.body.append(StandardCharsets.UTF_8.decode(copyBuffer));
    }

    public String getFullBody() {
        return this.body.toString();
    }
}
