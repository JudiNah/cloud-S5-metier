package projetS5.cloud.projetCloud.Model.Entities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findLastMessagesForId(String id) {
            return messageRepository.findDistinctByIdsenderAndIdReceiveOrderByDateSendDescTimeSendDesc(id, id);
        }    
}
