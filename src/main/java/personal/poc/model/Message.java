package personal.poc.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Message {
    private String language;
    private String content;
    private String languageDesc;
    private Date createdAt;
}
