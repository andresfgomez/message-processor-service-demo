package personal.poc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.poc.controller.DummyHandler;
import personal.poc.controller.MessageHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EndpointConfig {

    @Bean
    RouterFunction<ServerResponse> dummyRoutes(DummyHandler dummyHandler) {
        return route(GET("/hello"), dummyHandler::hello);
    }

    @Bean
    RouterFunction<ServerResponse> messageRoutes(MessageHandler handler) {
        return route(POST("/message"), handler::process);
    }
}
