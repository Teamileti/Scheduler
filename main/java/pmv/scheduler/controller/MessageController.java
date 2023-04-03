package pmv.scheduler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pmv.scheduler.configure.Scheduler;
import pmv.scheduler.model.Message;
import pmv.scheduler.service.MessageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    private Scheduler scheduler;

    @PostMapping("/newMessage")
    public Message addMessage(@RequestBody Message newMessage){
        return messageService.saveMessage(newMessage);
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody String message){
        template.convertAndSend("/topic/pushmessage", messageService.saveEmergencyMessage(message));
    }
    @PostMapping("/sendEmergency")
    public void sendEmergency(@RequestBody String message){
        template.convertAndSend("/topic/pushmessage", message);
    }

    @GetMapping("/getMessages")
    public List<Message> getMessages(){
        return messageService.getMessages();
    }

    @GetMapping("/displayCurrent")
    public void showMessage() {
        scheduler.init();
    }

    @DeleteMapping("/removeMessage")
    public ResponseEntity<Void> deleteMessage(@RequestParam("messageId") String id){ return messageService.deleteMessage(id);}

}
