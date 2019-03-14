package personal.poc.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:aws/s3.properties")
public class S3Config {

    @Value("${aws.access_key_id}")
    private String accessKey;

    @Value("${aws.secret_access_key}")
    private String secretKey;

    @Value("${s3.region}")
    private String region;

    private AWSCredentials getAWSCredentials(){
        return new BasicAWSCredentials(
                accessKey,
                secretKey
        );
    }

    @Bean(name = "s3Client")
    public AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getAWSCredentials()))
                .withRegion(region)
                .build();
    }
}
