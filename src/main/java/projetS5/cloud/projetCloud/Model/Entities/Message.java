package projetS5.cloud.projetCloud.Model.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "message")
public class Message {
    @Id
    private String id;
    private String idsender;
    private String idReceive;
    private String dateSend;
    private String timeSend;
    private String texto;
    private int etat;

    public void setDateSend(String dateSend) {
        this.dateSend = dateSend;
    }
    public void setEtat(int etat) {
        this.etat = etat;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setIdReceive(String idReceive) {
        this.idReceive = idReceive;
    }
    public void setIdsender(String idsender) {
        this.idsender = idsender;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }
    public String getDateSend() {
        return dateSend;
    }
    public int getEtat() {
        return etat;
    }
    public String getId() {
        return id;
    }
    public String getIdReceive() {
        return idReceive;
    }
    public String getIdsender() {
        return idsender;
    }
    public String getTexto() {
        return texto;
    }
    public String getTimeSend() {
        return timeSend;
    }

    // Constructeur avec des valeurs par défaut
    public Message() {
        this.dateSend = LocalDateTime.now().toLocalDate().toString();
        this.timeSend = LocalDateTime.now().toLocalTime().toString();
        this.etat =  5;
    }
    

    // Constructeurs, getters et setters
}
