package projetS5.cloud.projetCloud.Model.Entities;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    
    @Query("{$or:[{'idsender': ?0}, {'idReceive': ?0}]}")
    List<Message> findLastMessagesForId(@Param("id") String id);

    List<Message> findDistinctByIdsenderAndIdReceiveOrderByDateSendDescTimeSendDesc(String idsender, String idReceive);

    
}
