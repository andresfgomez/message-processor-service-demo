package personal.poc.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.poc.model.Message;
import personal.poc.service.MessageService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.Locale;

@Component
public class MessageHandler {

    private MessageService messageService;

    public MessageHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    public Mono<ServerResponse> process(ServerRequest request) {
        Mono<Void> flow = request.bodyToMono(Message.class).publishOn(Schedulers.elastic()).map(message -> {
            Locale locale = new Locale(message.getLanguage());
            message.setLanguageDesc(locale.getDisplayName());
            message.setCreatedAt(new Date());
            System.out.println("Current Thread In Controller: " + Thread.currentThread().getName());
            return message;
        }).flatMap(message -> messageService.save(message));
        return ServerResponse.noContent().build(flow).switchIfEmpty(ServerResponse.notFound().build());
    }

}
