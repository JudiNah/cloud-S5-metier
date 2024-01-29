package projetS5.cloud.projetCloud.Controllers;


import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Tables.PersonneAutentification;
import projetS5.cloud.projetCloud.Model.Utils.JwtToken;
import projetS5.cloud.projetCloud.Model.Views.Statistique;
import org.springframework.http.HttpHeaders;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatistiqueController {

    @GetMapping("state-vente/{annee}")
    public Map<String, Object> getStateVente(@PathVariable int annee,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)  throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Statistique[] statistiques = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            statistiques = new Statistique().stateVente(connection, annee);
            status = 200;
            titre = "Selection de statistique de vente avec succees";
            message = "Excellent , voici les statistique de vente";
        } catch (Exception e) {
            status = 500;
            titre = "Selection de statistique de vente echouee";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", statistiques);
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

    @GetMapping("state-annonce/{annee}")
    public Map<String, Object> getStateAnnonce(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int annee)  throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Statistique[] statistiques = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            statistiques = new Statistique().stateAnnonce(connection, annee);
            status = 200;
            titre = "Selection de statistique d'annonces avec succees";
            message = "Excellent , voici les statistique d'annonces";
        } catch (Exception e) {
            status = 500;
            titre = "Selection de statistique d'annonces echouee";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", statistiques);
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

    @GetMapping("state-utilisateur/{annee}")
    public Map<String, Object> getStateUtilisateur(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @PathVariable int annee) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Statistique[] statistiques = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            statistiques = new Statistique().stateUtilisateurs(connection, annee);
            status = 200;
            titre = "Selection de statistique d'utilisateurs avec succees";
            message = "Excellent , voici les statistique d'utilisateurs";
        } catch (Exception e) {
            status = 500;
            titre = "Selection de statistique d'utilisateurs echouee";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", statistiques);
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
