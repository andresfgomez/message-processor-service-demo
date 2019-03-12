package personal.poc.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;

@Document
@Data
public class Message {
    private String language;
    private String content;
    private Locale locale;
}
