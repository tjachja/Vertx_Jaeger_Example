package io.vertx.verticles;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerRequest;

public class WebVerticle extends AbstractVerticle {
	Configuration configuration = new Configuration("Webserver-Service");
	Tracer tracer = configuration.getTracerBuilder().build();
    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(httpRequest -> {
                	handleHttpRequest(httpRequest);
                })
                .listen(8083);
    }

    private void handleHttpRequest(final HttpServerRequest httpRequest) {

        /* Invoke using the event bus. */
        vertx.eventBus().send(Services.HELLO_WORLD.toString(), HelloWorldOperations.SAY_HELLO_WORLD.toString(), response -> {
        	Span span = tracer.buildSpan("handleHttpRequest").start();
            if (response.succeeded())
                /* Send the result from HelloWorldService to the http connection. */
                httpRequest.response().end(response.result().body().toString());
            else 
                httpRequest.response().setStatusCode(500).end(response.cause().getMessage());
            span.finish();
        });
    }
}
