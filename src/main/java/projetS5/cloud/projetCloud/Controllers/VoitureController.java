package projetS5.cloud.projetCloud.Controllers;

import org.springframework.beans.factory.support.ManagedList;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import projetS5.cloud.projetCloud.Context.PgConnection;
import projetS5.cloud.projetCloud.Model.Bag;
import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Objects.Client;
import projetS5.cloud.projetCloud.Model.Tables.*;
import projetS5.cloud.projetCloud.Model.Utils.JwtToken;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class VoitureController {
    @GetMapping("elementNecessaire")
    public Map<String, Object> element_necessaire(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
        // System.out.println(authorizationHeader);
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Vector donne = new Vector();
        Connection connection = null;
        try {
        connection = ConnectionPostgres.connectDefault();
        connection.setAutoCommit(false);
        JwtToken jwtToken = new JwtToken();
        String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
        PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
        personneAutentification.setAdmin(true);
        personneAutentification.authentificationByIdAndRole(connection);
        System.out.println(idAdmin);
        donne.add("categories");
        donne.add("marques");
        donne.add("types-carburant");
        donne.add("transmissions");
        donne.add("freinages");
        donne.add("equipements-internes");
    
            status = 200;
            titre = "Prendre les elements necessaire a reussi";
            message = "Excellent , vous avez prendre tout les elements necessaire ";
            
        } catch (Exception e) {
            status = 500;
            titre = "Prendre de l'element necessaire a echoue a echoue";
            message = e.getMessage();
        } finally {
            resultat.put("data", donne);
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
    @GetMapping("categories")
    public Bag ListCategoriesVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        int status = 0;
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<CategorieVoiture> categorieVoitureList = new CategorieVoiture().read(connection);
            bag = new Bag(null, null, categorieVoitureList);
            status = 200;
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
            status = 500;
        }
        finally {
            if (connection!=null) {   
                if (!connection.isClosed() && connection != null)
                connection.close();
            }
        }

        return bag;
    }

    @GetMapping("marques")
    public Bag ListeMarquesVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<MarqueVoiture> marqueVoitureList = new MarqueVoiture().read(connection);
            bag = new Bag(null, null, marqueVoitureList);
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @GetMapping("types-carburant") 
    public Bag ListeTypesCarburantsVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<TypeCarburantVoiture> typeCarburantVoitureList = new TypeCarburantVoiture().read(connection);
            bag = new Bag(null, null, typeCarburantVoitureList);
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @GetMapping("transmissions")
    public Bag ListeTransmissionsVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<TransmissionVoiture> transmissionVoitureList = new TransmissionVoiture().read(connection);
            bag = new Bag(null, null, transmissionVoitureList);
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }


    @GetMapping("freinages") 
    public Bag ListeFreinageVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<FreignageVoiture> freignageVoitureList = new FreignageVoiture().read(connection);
            bag = new Bag(null, null, freignageVoitureList);
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @GetMapping("equipements-internes") 
    public Bag ListeEquipemenstInternesVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            List<EquipementInterne> equipementInterneList = new EquipementInterne().read(connection);
            bag = new Bag(null, null, equipementInterneList);
        }
        catch (Exception e) {
            bag = new Bag("Selection", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    // ---------------- CREATE -----------------------------------------------------------
    @PostMapping("/categorie")
    public Map<String, Object> createCategorie(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");

            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            categorieVoiture.create(connection);
    
           
            status = 200;
            titre = "Creation de categorie effectue";
            message = "Bravo , vous avez creer une nouvelle categorie de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de categorie a échoué";
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

    @PostMapping("/type-carburant")
    public Map<String, Object> createTypeCarburant(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            TypeCarburantVoiture typeCarburantVoiture = new TypeCarburantVoiture();
            typeCarburantVoiture.setNom(name);
            typeCarburantVoiture.create(connection);
    
           
            status = 200;
            titre = "Creation de type caburant effectue";
            message = "Bravo , vous avez creer une nouvelle type de carburant de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de type de carburant a échoué";
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

    @PostMapping("/transmission")
    public Map<String, Object> createTransmission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            TransmissionVoiture transmission = new TransmissionVoiture();
            transmission.setNom(name);
            transmission.create(connection);
    
           
            status = 200;
            titre = "Creation de trasmission effectue";
            message = "Bravo , vous avez creer une nouvelle transmission de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de transmission a échoué";
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

    @PostMapping("/freinage")
    public Map<String, Object> createFreinage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            FreignageVoiture transmission = new FreignageVoiture();
            transmission.setNom(name);
            transmission.create(connection);
    
           
            status = 200;
            titre = "Creation de freinage effectue";
            message = "Bravo , vous avez creer une nouvelle freinage de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de freinage a échoué";
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

    @PostMapping("/equipement-interne")
    public Map<String, Object> createEquipementInterne(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        
        Connection connection =  null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            EquipementInterne transmission = new EquipementInterne();
            transmission.setNom(name);
            transmission.create(connection);
    
           
            status = 200;
            titre = "Creation de equipement interne effectue";
            message = "Bravo , vous avez creer une nouvelle equipement interne de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de equipement interne a échoué";
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

    @PostMapping("/marque")
    public Map<String, Object> createMarque(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
    
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");
            String dateCreation = (String) requestBody.get("dateCreation"); 
            donnes.add(name);
            donnes.add(description);
            MarqueVoiture marque = new MarqueVoiture();
            marque.setNom(name);
            marque.setDescription(description);
            marque.setDateCreation(Date.valueOf(dateCreation));
            marque.create(connection);
    
           
            status = 200;
            titre = "Creation de marque effectue";
            message = "Bravo , vous avez creer une nouvelle marque de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de categorie a échoué";
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
   
    // ---------------- MODIFIER -------------------------------
    @PutMapping("catego/{id}")
    public Map<String, Object> updateCategorie(@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");
            
            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            categorieVoiture.update(connection);
    
           
            status = 200;
            titre = "Modification de categorie effectue";
            message = "Bravo , vous avez modifier une categorie de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de categorie a échoué";
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

    @PutMapping("categorie/{id}")
    public Map putMethodName(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur
        Connection connection = null;
        try {
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");
             connection = ConnectionPostgres.connectDefault();
             connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            System.out.println(id+" "+name+" "+description);
            categorieVoiture.update(connection);
    
           
            status = 200;
            titre = "Modification de categorie effectue";
            message = "Bravo , vous avez modifier une categorie de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de categorie a échoué";
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

    @PutMapping("/marque/{id}")
    public Map<String, Object> updateMarque(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");
            String dateCreation = (String) requestBody.get("dateCreation"); 
            donnes.add(name);
            donnes.add(description);
            MarqueVoiture marque = new MarqueVoiture();
            marque.setId(id);
            marque.setNom(name);
            marque.setDescription(description);
            marque.setDateCreation(Date.valueOf(dateCreation));
            marque.update(connection);
    
           
            status = 200;
            titre = "Modification de marque effectue";
            message = "Bravo , vous avez modeifier une nouvelle marque de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de marque a échoué";
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

    @PutMapping("/type-carburant/{id}")
    public Map<String, Object> updateTypeCarburant(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            TypeCarburantVoiture typeCarburantVoiture = new TypeCarburantVoiture();
            typeCarburantVoiture.setId(id);
            typeCarburantVoiture.setNom(name);
            typeCarburantVoiture.update(connection);
    
           
            status = 200;
            titre = "Modification de type caburant effectue";
            message = "Bravo , vous avez modifier une nouvelle type de carburant de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de type de carburant a échoué";
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

    @PutMapping("/transmission/{id}")
    public Map<String, Object> updateTransmission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            TransmissionVoiture typeCarburantVoiture = new TransmissionVoiture();
            typeCarburantVoiture.setId(id);
            typeCarburantVoiture.setNom(name);
            typeCarburantVoiture.update(connection);
    
           
            status = 200;
            titre = "Modification de transmission effectue";
            message = "Bravo , vous avez modifier une transmission de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de transmission a échoué";
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

    @PutMapping("/freinage/{id}")
    public Map<String, Object> updatefreinage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            FreignageVoiture freignageVoiture = new FreignageVoiture();
            freignageVoiture.setId(id);
            freignageVoiture.setNom(name);
            freignageVoiture.update(connection);
    
           
            status = 200;
            titre = "Modification de freinage effectue";
            message = "Bravo , vous avez modifier une freinage de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de freinage a échoué";
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

    @PutMapping("/equipement-interne/{id}")
    public Map<String, Object> updateEquipementInterne(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            String name = (String) requestBody.get("nom");

            donnes.add(name);
            EquipementInterne equipementInterne = new EquipementInterne();
            equipementInterne.setId(id);
            equipementInterne.setNom(name);
            equipementInterne.update(connection);
    
           
            status = 200;
            titre = "Modification de l'equipement interne effectue";
            message = "Bravo , vous avez modifier un equipement ineterne de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de equipement interne a échoué";
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

    /* --------------------------- D E L E T E -------------------------------------- */

    @DeleteMapping("categorie/{id}")
    public Map deletcategorie(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur
        Connection connection = null;

        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.delete(connection);
    
           
            status = 200;
            titre = "Suppression de categorie effectue";
            message = "Bravo , vous avez supprimer une categorie de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Suppression de categorie a échoué";
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

    @DeleteMapping("marque/{id}")
    public Map deletemarque(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            MarqueVoiture marque = new MarqueVoiture();
            marque.setId(id);
            marque.delete(connection);
    
           
            status = 200;
            titre = "Suppression de marque effectue";
            message = "Bravo , vous avez supprimer une marque de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Suppression de marque a échoué";
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
        @DeleteMapping("/type-carburant/{id}")
        public Map<String, Object> deletetypeCarburant(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception {
            Map<String, Object> resultat = new HashMap<>();
            int status = 0;
            String titre = null;
            String message = null;
            Map<String, Object> data = new HashMap<>();
            Vector<String> donnes = new Vector<>();
            Connection connection = null;

            try {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                JwtToken jwtToken = new JwtToken();
                String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
                PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
                personneAutentification.setAdmin(true);
                personneAutentification.authentificationByIdAndRole(connection);

                TypeCarburantVoiture typeCarburantVoiture = new TypeCarburantVoiture();
                typeCarburantVoiture.setId(id);
                typeCarburantVoiture.delete(connection);

            
                status = 200;
                titre = "Suppression de type caburant effectue";
                message = "Bravo , vous avez supprimer une nouvelle type de carburant de voiture";
            } catch (Exception e) {
                status = 500;
                titre = "Suppression de type de carburant a échoué";
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

        @DeleteMapping("/transmission/{id}")
        public Map<String, Object> deletetransmission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception{
            Map<String, Object> resultat = new HashMap<>();
            int status = 0;
            String titre = null;
            String message = null;
            Map<String, Object> data = new HashMap<>();
            Vector<String> donnes = new Vector<>();
            Connection connection = null;
            try {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                JwtToken jwtToken = new JwtToken();
                String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
                PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
                personneAutentification.setAdmin(true);
                personneAutentification.authentificationByIdAndRole(connection);

                TransmissionVoiture transmissionVoiture = new TransmissionVoiture();
                transmissionVoiture.setId(id);
                transmissionVoiture.delete(connection);

            
                status = 200;
                titre = "Suppression de transmission effectue";
                message = "Bravo , vous avez supprimer transmission de voiture";
            } catch (Exception e) {
                status = 500;
                titre = "Suppression de transmission a échoué";
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

        @DeleteMapping("/freinage/{id}")
        public Map<String, Object> deletefreinage(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception{
            Map<String, Object> resultat = new HashMap<>();
            int status = 0;
            String titre = null;
            String message = null;
            Map<String, Object> data = new HashMap<>();
            Vector<String> donnes = new Vector<>();
            Connection connection = null;
            try {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                JwtToken jwtToken = new JwtToken();
                String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
                PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
                personneAutentification.setAdmin(true);
                personneAutentification.authentificationByIdAndRole(connection);

                FreignageVoiture freignageVoiture = new FreignageVoiture();
                freignageVoiture.setId(id);
                freignageVoiture.delete(connection);

            
                status = 200;
                titre = "Suppression de frenage effectue";
                message = "Bravo , vous avez supprimer frenage de voiture";
            } catch (Exception e) {
                status = 500;
                titre = "Suppression de freinage a échoué";
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

        @DeleteMapping("/equipement-interne/{id}")
        public Map<String, Object> deleteEquipementInterne(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id) throws Exception {
            Map<String, Object> resultat = new HashMap<>();
            int status = 0;
            String titre = null;
            String message = null;
            Map<String, Object> data = new HashMap<>();
            Vector<String> donnes = new Vector<>();
            Connection connection = null;
            try {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                JwtToken jwtToken = new JwtToken();
                String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
                PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
                personneAutentification.setAdmin(true);
                personneAutentification.authentificationByIdAndRole(connection);

                EquipementInterne transmissionVoiture = new EquipementInterne();
                transmissionVoiture.setId(id);
                transmissionVoiture.delete(connection);

            
                status = 200;
                titre = "Suppression de equipement interne effectue";
                message = "Bravo , vous avez supprimer un equipement interne de voiture";
            } catch (Exception e) {
                status = 500;
                titre = "Suppression de equipement interne a échoué";
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

 
    @PutMapping("commission/{id}")
    public Map<String, Object> updateCommission(@PathVariable String id,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            double prixMin = Double.parseDouble( (String) requestBody.get("prix_min"));
            double prixMax = Double.parseDouble( (String) requestBody.get("prix_max"));
            double tauxCommission = Double.parseDouble( (String) requestBody.get("taux_commission"));
            Date date = Date.valueOf((String) requestBody.get("date"));

            Commission commission = new Commission();
            commission.setTauxCommission(tauxCommission);
            commission.setDate(date);
            commission.setPrixMin(prixMin);
            commission.setPrixMax(prixMax);

            commission.update(null);

            status = 200;
            titre = "Modification de commission effectue";
            message = "Bravo , vous avez modifier une commission de vente";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de commission a échoué";
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

}
