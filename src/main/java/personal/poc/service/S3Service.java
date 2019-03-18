package personal.poc.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import personal.poc.model.S3Information;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class S3Service {

    private AmazonS3 s3Client;

    public S3Service(@Qualifier("s3Client") AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public Mono<Void> save(S3Information s3Information) {

        return Mono.fromSupplier(() -> {
                    log.debug("... Uploading to s3 ...");

                    s3Client.putObject(
                            s3Information.getBucketName(),
                            s3Information.getPathInBucket(),
                            s3Information.getInfoToSave(),
                            s3Information.getMetadata());

                    return null;
                }
        );

    }
}
