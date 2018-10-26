package io.vertx.common;

import io.opentracing.propagation.TextMap;
import io.vertx.core.MultiMap;

import java.util.Iterator;
import java.util.Map;


public class VertxMultiMapExtractAdapter implements TextMap {
    private final MultiMap headers;

    public VertxMultiMapExtractAdapter(final MultiMap headers) {
        this.headers = headers;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return headers.entries().iterator();
    }

    @Override
    public void put(String key, String value) {
        throw new UnsupportedOperationException(
                "VertxMessageExtractAdapter should only be used with Tracer.extract()");
    }
}