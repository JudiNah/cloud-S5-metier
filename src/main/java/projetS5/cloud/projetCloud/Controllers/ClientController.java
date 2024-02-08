package projetS5.cloud.projetCloud.Controllers;

import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Entities.Admin;
import projetS5.cloud.projetCloud.Model.Objects.Client;
import projetS5.cloud.projetCloud.Model.Tables.Personne;
import projetS5.cloud.projetCloud.Model.Tables.PersonneAutentification;
import projetS5.cloud.projetCloud.Model.Utils.JwtToken;
import projetS5.cloud.projetCloud.Model.Views.VAnnonce;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class ClientController {
    @PostMapping("/create_compte_client")
    public Map<String, Object> create_compte_client(@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Connection connection = null;
        try {
            String name = (String) requestBody.get("name");
            String password = (String) requestBody.get("password");
            name = name.trim();
            password = password.trim();
            if(name.length()==0 && password.length()==0){
                throw new Exception("Nom et mot de passe invalide");
            }else
            if (name.length()==0) {
                throw new Exception("Nom invalide");
            }else
            if (password.length()==0) {
                throw new Exception("Mot de passe invalide");
            }
            Client client = new Client();
            client.setName(name);
            client.setPassword(password);
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            if (client.clientisExist(connection)) {
                throw new Exception("Modifier l'information s'il vous plait");
            }
            client.addNewClient(connection);
            status = 200;
            titre = "Creation de compte a fait avec succees";
            message = "Excellent , votre compte a ete bien creer";
            donnes.put("name", name);
            donnes.put("password", password);
            
        } catch (Exception e) {
            status = 500;
            titre = "Creation de compte a echoue";
            message = e.getMessage();
        } finally {
            resultat.put("data", donnes);
            resultat.put("status", status);
            resultat.put("titre", titre);
            resultat.put("message", message);
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }
    
    // login client
    @PostMapping("/authentification_client")
    public Map<String, Object> authentification_client(@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        String name = (String) requestBody.get("username");
        String password = (String) requestBody.get("password");
        Connection connection = null;
        try {
            name = name.trim();
            password = password.trim();
            if(name.length()==0 && password.length()==0){
                throw new Exception("Nom et mot de passe invalide");
            }else
            if (name.length()==0) {
                throw new Exception("Nom invalide");
            }else
            if (password.length()==0) {
                throw new Exception("Mot de passe invalide");
            }
            Client client = new Client();
            client.setName(name);
            client.setPassword(password);
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            client.connect(connection);

            // si le connection est reussi
            String secretKey = "projetclouds5";

            // Définir la date d'expiration à 2 jours à partir de maintenant
            Date expirationDate = new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);

            String token = Jwts.builder()
                    .setSubject("tokentheclient")
                    .claim("name", client.getName())
                    .claim("password", client.getPassword())
                    .claim("idClient", client.getIdClient())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
                resultat.put("token", token);
            // PRENDRE LE TOKEN
            // Claims claims = Jwts.parser()
            // .setSigningKey("projetclouds5") // Remplacez "votre-cle-secrete" par la même clé secrète que celle utilisée pour signer le token
            // .parseClaimsJws(token)
            // .getBody();

            // String nameintoken = claims.get("name", String.class);
            // String passwordintoeken = claims.get("password", String.class);
            // String idClients = claims.get("idClient", String.class);

            // System.out.println(nameintoken+" ; "+passwordintoeken+" ; "+idClients);
            status = 200;
            titre = "Connection du client reussi";
            message = "Excellent , vous avez bien connecter a votre compte , "+client.getName();
            
        } catch (Exception e) {
            status = 500;
            titre = "Connection de compte a echoue";
            message = e.getMessage();
        } finally {
            donnes.put("name", name);
            donnes.put("password", password);
            resultat.put("data", donnes);
            resultat.put("status", status);
            resultat.put("titre", titre);
            resultat.put("message", message);
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }

    /*---------------------------------------------------------- */
    @PostMapping("/client")
    public Map<String, Object> createClient(@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            // authentification
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            String email = getStringFromRequestBody(requestBody, "email");
            String password = getStringFromRequestBody(requestBody, "password");
            boolean isAdmin = false;
            // personne
            String nom = getStringFromRequestBody(requestBody, "nom");
            String prenom = getStringFromRequestBody(requestBody, "prenom");
            String address = getStringFromRequestBody(requestBody, "address");
            String telephone = getStringFromRequestBody(requestBody, "telephone");
            int sexe = getIntFromRequestBody(requestBody, "sexe");
            String dateNaissance = getStringFromRequestBody(requestBody, "dateNaissance");
            data.put("email", email);
            data.put("password", password);
            data.put("nom", nom);
            data.put("prenom", prenom);
            data.put("address", address);
            data.put("telephone", telephone);
            data.put("sexe", sexe);
            data.put("dateNaissance", dateNaissance);

            Personne personne = new Personne();
            personne.setNom(nom);
            personne.setPrenom(prenom);
            personne.setSexe(sexe);
            personne.setTelephone(telephone);
            personne.setAddress(address);
            if (Date.valueOf(dateNaissance) == null) {
                throw new Exception("Date de naissance invalide");
            }
            personne.setDateNaissance(Date.valueOf(dateNaissance));
            
            String idPersonne = personne.create(connection);
            PersonneAutentification personneAutentification = new PersonneAutentification(null, email, password, isAdmin, personne);
            personneAutentification.setPersonneId(idPersonne);
            personneAutentification.create(connection);
            


            status = 200;
            titre = "Creation de nouvelle utilisateur du VaikaNet";
            message = "Bravo , vous avez creer une nouvelle compte";
        } catch (Exception e) {
            status = 500;
            titre = "Creation d'utilisateur a échoué";
            e.printStackTrace();
            message = e.getMessage();
        } finally {
            if (connection!=null) {
                connection.commit();
                connection.close();
            }
            resultat.put("data", data);
            resultat.put("status", status);
                resultat.put("titre", titre);
                resultat.put("message", message);
        }

        return resultat;
    }

    private String getStringFromRequestBody(Map<String, Object> requestBody, String key) throws Exception {
        String value = (String) requestBody.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new Exception(key + " invalide");
        }
        return value.trim();
    }

    private int getIntFromRequestBody(Map<String, Object> requestBody, String key) throws Exception {
        Object value = requestBody.get(key);
        if (value instanceof Integer) {
            int number = (int) value;
            if (number>=0) {
                return number;
            }else{
                throw new Exception(key + " invalide");
            }
        } else {
            throw new Exception(key + " invalide");
        }
    }

    @GetMapping("connect_token")
    public Map<String, Object> getAllAnnonceValideByIdClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String id = jwtToken.checkBearer(authorizationHeader, "client");
            PersonneAutentification personneAutentification = new PersonneAutentification(id);
            personneAutentification.setAdmin(null);
            personneAutentification.authentificationByIdAndRole(connection); 
            personneAutentification.setAdmin(false);
            personneAutentification = personneAutentification.getAuthentificationPersonneById(connection);
            resultat.put("personne", personneAutentification);

            status = 200;
            titre = "Connection avec le token est fait avec succees";
            message = "Excellent ,vous etes connecter";
        } catch (Exception e) {
            status = 500;
            titre = "Vous n'etes pas connecter";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("status", status);
            resultat.put("titre", titre);
            resultat.put("message", message);
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }

}
