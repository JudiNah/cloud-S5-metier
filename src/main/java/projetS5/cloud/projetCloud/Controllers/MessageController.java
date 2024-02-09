package projetS5.cloud.projetCloud.Controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Entities.Message;
import projetS5.cloud.projetCloud.Model.Entities.MessageRepository;
import projetS5.cloud.projetCloud.Model.Tables.Annonce;
import projetS5.cloud.projetCloud.Model.Tables.PersonneAutentification;
import projetS5.cloud.projetCloud.Model.Utils.JwtToken;

@RestController
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/message")
    public Message createMessage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws SQLException {
        
        int status = 0;
        String titre = null;
        Connection connection = null;
        Message message = new Message();
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idClient = jwtToken.checkBearer(authorizationHeader, "client");
            PersonneAutentification personneAutentification = new PersonneAutentification(idClient);
            personneAutentification.setAdmin(false);
            personneAutentification.authentificationByIdAndRole(connection);
            personneAutentification = personneAutentification.getAuthentificationPersonneById(connection);
            message.setIdsender(personneAutentification.getPersonne().getId());
            message.setIdReceive((String) requestBody.get("idReceive"));
            message.setTexto((String) requestBody.get("texto"));
            
        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {

            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }   
        }
        
        return messageRepository.save(message);
    }
}
