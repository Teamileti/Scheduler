package pmv.scheduler.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pmv.scheduler.model.Message;
import pmv.scheduler.service.MessageService;
import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;


@Component
@Configuration
@EnableScheduling
public class Scheduler extends AbstractQueue<Message>{
    private volatile boolean running = true;

    private LinkedList<Message> messages;

    private SimpMessagingTemplate template;

    private final MessageService messageService;

    public Scheduler(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    public void terminate(){
        running = false;
    }

  //  @PostConstruct
    @Scheduled(fixedRate = 2000)
    public void init() {
        while (running){
            try {
                this.messages = messageService.getMessages();
          Message lastMessage = messages.peekLast(); //get the latest added message
            assert lastMessage != null;
       if (Objects.equals(lastMessage.getType().getPriority(), "HIGH")){ //check if it has a high priority
                 System.out.println(lastMessage.getContent());
                 template.convertAndSend("/topic/pushmessages",lastMessage.getContent());
                 messageService.deleteMessage(lastMessage.getId()); //remove from queue, this message is shown only once
                Thread.sleep(lastMessage.getType().getDuration());
                  //terminate();
              }
                else {
                  int count = 0;
                   int length = messages.size();
                 while (count < length) {
                     if (Objects.equals(messages.get(count).getType().getPriority(), "HIGH")) {
                         System.out.println(messages.get(count).getContent());
                         template.convertAndSend("/topic/pushmessages", messages.get(count).getContent());
                         messageService.deleteMessage(messages.get(count).getId());
                         Thread.sleep(messages.get(count).getType().getDuration());
                         //terminate();
                     } else {
                         System.out.println(messages.get(count).getContent());
                         template.convertAndSend("/topic/pushmessages", messages.get(count).getContent());
                         Thread.sleep(messages.get(count).getType().getDuration());
                         count++;
                     }
                 }
            }
           }
            catch (InterruptedException e) {
                e.printStackTrace();
               running = false;
            }
         }
    }

    @Override
    public Iterator<Message>iterator() {
        return messages.iterator();
    }

    @Override
    public int size() {
        return messages.size();
    }

    @Override
    public boolean offer(Message message) {
        if (message == null) return false;
        messages.add(message);
        return true;
    }

    @Override
    public Message poll() {
        Iterator<Message> messageIterator = messages.iterator();
        Message message = messageIterator.next();
        if(message != null){
            messageIterator.remove();
            return message;
        }
        return null;
    }

    @Override
    public Message peek() {
        return messages.getFirst();
    }
}


