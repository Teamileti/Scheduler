package pmv.scheduler.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pmv.scheduler.model.Message;
import pmv.scheduler.model.Type;
import pmv.scheduler.repository.MessageRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message newMessage) {
        return messageRepository.save(newMessage);
    }

    public LinkedList<Message> getMessages() {
        //messages are saved ona  linked list. Each node has two pointers for next and previous element
        LinkedList<Message> messages = new LinkedList(messageRepository.findAll());
        return messages;
    }

    public ResponseEntity<Void> deleteMessage(String id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message != null){
            messageRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public Message saveEmergencyMessage(String message) {
        Message pmv = new Message();
        Type type = new Type();
        type.setDuration(7000L);
        type.setPriority("HIGH");
        pmv.setContent(message);
        pmv.setType(type);
        return messageRepository.save(pmv);
    }
}
