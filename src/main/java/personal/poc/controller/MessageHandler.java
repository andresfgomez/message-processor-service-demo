package personal.poc.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.poc.model.Message;
import personal.poc.service.MessageService;
import reactor.core.publisher.Mono;

@Component
public class MessageHandler {

    private MessageService messageService;

    public MessageHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    public Mono<ServerResponse> process(ServerRequest request) {
        Mono<Void> flow = request.bodyToMono(Message.class).flatMap(message -> messageService.save(message));
        return ServerResponse.noContent().build(flow).switchIfEmpty(ServerResponse.notFound().build());
    }

}
