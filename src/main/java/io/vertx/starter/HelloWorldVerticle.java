package io.vertx.starter;

import io.jaegertracing.Configuration;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.SpanContext;
import io.opentracing.propagation.Format;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class HelloWorldVerticle extends AbstractVerticle {
	Configuration configuration = new Configuration("Hello-World-Service");
	Tracer tracer = configuration
	            .getTracerBuilder()
	            .build();
	
	  @Override
	  public void start() {
	      vertx.eventBus().consumer(Services.HELLO_WORLD.toString(), message -> {
	    	  Span span = tracer.buildSpan("send-hello-world").start();
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
