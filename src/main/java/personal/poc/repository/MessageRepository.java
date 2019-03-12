package personal.poc.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import personal.poc.model.Message;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, String> {
}
