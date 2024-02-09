package projetS5.cloud.projetCloud.Model.Entities;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface MessageRepository extends MongoRepository<Message, String> {
    // Les méthodes CRUD sont déjà disponibles
}


