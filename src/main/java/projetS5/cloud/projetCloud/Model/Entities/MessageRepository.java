package projetS5.cloud.projetCloud.Model.Entities;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByIdsenderAndIdReceiveOrderByDateSend(String idsender, String idReceive);
}

