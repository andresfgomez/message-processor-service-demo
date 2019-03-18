package personal.poc.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;
import personal.poc.model.Message;
import personal.poc.model.MessageSnapshot;
import personal.poc.model.S3Information;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@Slf4j
@PropertySource("classpath:aws/s3.properties")
public class MessageSnapshotService {

    @Value("${s3.bucket}")
    private String bucketName;

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final S3Service s3Service;

    public MessageSnapshotService(ReactiveMongoTemplate reactiveMongoTemplate, S3Service s3Service) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.s3Service = s3Service;
    }

    public Flux<MessageSnapshot> saveSnapshotOfMessageGroupedByLanguage() {
        Flux<MessageSnapshot> messageSnapshotGroupedByLanguage = groupMessageByLanguage();
        saveSnapshotInS3(messageSnapshotGroupedByLanguage);
        return messageSnapshotGroupedByLanguage;
    }

    private Flux<MessageSnapshot> groupMessageByLanguage(){
        TypedAggregation<Message> aggregation = newAggregation(Message.class,
                group("languageDesc").count().as("times"),
                project("times").and("languageDesc").previousOperation());

        return reactiveMongoTemplate.
                aggregate(aggregation, MessageSnapshot.class);
    }

    private void saveSnapshotInS3(Flux<MessageSnapshot> messageSnapshotFlux) {
        Mono<byte[]> snapshotJsonBytes = convertSnapshotToJsonByteArray(messageSnapshotFlux);
        saveJsonInS3(snapshotJsonBytes);
    }

    private void saveJsonInS3(Mono<byte[]> snapshotJsonBytes) {
        snapshotJsonBytes.subscribe(snapshotByte -> {
            InputStream snapshotInputStream = new ByteArrayInputStream(snapshotByte);

            Long contentLength = Long.valueOf(snapshotByte.length);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);

            s3Service.save(S3Information.builder()
                    .bucketName(bucketName)
                    .pathInBucket("snapshotFileByService")
                    .infoToSave(snapshotInputStream)
                    .metadata(metadata)
                    .build()).subscribe();
        });
    }

    private Mono<byte[]> convertSnapshotToJsonByteArray(Flux<MessageSnapshot> messageSnapshotFlux) {
        ObjectMapper mapper = new ObjectMapper();

        return messageSnapshotFlux.collectList().flatMap(messageSnapshot -> {
            try {
                return Mono.just(mapper.writeValueAsBytes(messageSnapshot));
            } catch (JsonProcessingException e) {
                return  Mono.justOrEmpty(Optional.empty());
            }
        } );
    }
}
