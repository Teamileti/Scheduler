package pmv.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "message")
public class Message {
    @Id
    private String id;
    private String content;
    private Type type;

    public Message(String content, Type type, String time) {
    }
}
