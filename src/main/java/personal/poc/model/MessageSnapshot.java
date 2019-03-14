package personal.poc.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class MessageSnapshot {

    @Field("languageDesc")
    private String language;
    private Long times;
}
