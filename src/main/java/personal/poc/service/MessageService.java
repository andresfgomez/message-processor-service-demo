package personal.poc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import personal.poc.model.Message;
import personal.poc.repository.MessageRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<Void> save(Message message) {
        return Mono.fromSupplier(() -> {
            System.out.println("Current Thread In Service: " + Thread.currentThread().getName());
            messageRepository.save(message).subscribe();
            return null;
        }).subscribeOn(Schedulers.parallel()).then();
    }
    
}
