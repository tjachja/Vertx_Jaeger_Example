package io.vertx.verticles;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class HelloWorldVerticle extends AbstractVerticle {
	Configuration configuration = new Configuration("Hello-World-Service");
    Tracer tracer = configuration.getTracerBuilder().build();
	
	  @Override
	  public void start() {
          String corrID = "7777777";
          //MultiMap headers;

          //SpanContext spanCtx = tracer.extract(Format.Builtin.HTTP_HEADERS, new VertxMultiMapExtractAdapter(headers));

	      vertx.eventBus().consumer(Services.HELLO_WORLD.toString(), message -> {
              Span span = tracer.buildSpan("send-hello-world").start();
              //Span span = tracer.buildSpan("send-hello-world").asChildOf(spanCtx).start();
	          dispatchMessage(message);
	          span.finish();
	      });
	  }

	  private void dispatchMessage(final Message<Object> message) {
	      try {
	    	  final HelloWorldOperations operation = HelloWorldOperations.valueOf(message.body().toString());
	    	  switch (operation) {
              	case SAY_HELLO_WORLD:
              		message.reply("HELLO WORLD");
              		break;
              	default:
              		message.reply("Unsupported operation");  
	    	  }
	      } catch (final Exception ex) {
	    	  System.out.println("Error");
	      }
	  }

	}
