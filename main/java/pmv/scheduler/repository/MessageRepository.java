package pmv.scheduler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pmv.scheduler.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
