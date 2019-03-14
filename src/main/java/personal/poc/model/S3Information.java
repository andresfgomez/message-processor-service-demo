package personal.poc.model;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.io.InputStream;

@Data
@Builder
public class S3Information {

    @NonNull
    private String bucketName;

    @NonNull
    private String pathInBucket;

    @NonNull
    private InputStream infoToSave;

    @NonNull
    private ObjectMetadata metadata;
}
