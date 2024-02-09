package projetS5.cloud.projetCloud.Model.Entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> findAllMessagesBetweenIdsSortedByDate(String id1, String id2) {
        List<String> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);
        return messageRepository.findByIdsenderInAndIdReceiveInOrderByDateSend(ids, ids);
    }
}

