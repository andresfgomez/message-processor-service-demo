package personal.poc.service;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import personal.poc.model.Message;
import personal.poc.model.MessageSnapshot;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class MessageSnapshotService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MessageSnapshotService(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Scheduled(fixedDelayString = "5000")
    public Flux<personal.poc.model.MessageSnapshot> groupMessageByLanguage(){
        TypedAggregation<Message> aggregation = newAggregation(Message.class,
                group("language").count().as("times"),
                project("times").and("language").previousOperation());

        Flux<personal.poc.model.MessageSnapshot> messageStatisticsDTOFlux = reactiveMongoTemplate.
                aggregate(aggregation, MessageSnapshot.class);

        messageStatisticsDTOFlux.doOnError(Exception::new).subscribe(messageSnapshot ->
                System.out.println("L: " + messageSnapshot.getLanguage() + " - " + "Times: " + messageSnapshot.getTimes()));

        return messageStatisticsDTOFlux;
    }
}
