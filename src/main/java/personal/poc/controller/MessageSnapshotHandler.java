package personal.poc.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import personal.poc.model.MessageSnapshot;
import personal.poc.service.MessageSnapshotService;
import reactor.core.publisher.Mono;

@Component
public class MessageSnapshotHandler {

    private final MessageSnapshotService messageSnapshotService;

    public MessageSnapshotHandler(MessageSnapshotService messageSnapshotService) {
        this.messageSnapshotService = messageSnapshotService;
    }

    public Mono<ServerResponse> generateSnapshot() {
        return ServerResponse.ok()
                .body(messageSnapshotService.saveSnapshotOfMessageGroupedByLanguage(), MessageSnapshot.class)
                .switchIfEmpty(ServerResponse.noContent().build());
    }
}
