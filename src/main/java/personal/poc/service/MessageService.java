package personal.poc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import personal.poc.model.Message;
import personal.poc.repository.MessageRepository;
import reactor.core.publisher.Mono;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Mono<Void> save(Message message) {
        return Mono.fromSupplier(() -> {
            messageRepository.save(message).subscribe();
            return null;
        });
    }
    
}
