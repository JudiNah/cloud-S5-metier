package projetS5.cloud.projetCloud.Controllers;

import org.springframework.beans.factory.support.ManagedList;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Map<String, Object> element_necessaire(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        // System.out.println(authorizationHeader);
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Vector donne = new Vector();
        try {
        JwtToken jwtToken = new JwtToken();
        String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
        PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
        personneAutentification.setAdmin(true);
        personneAutentification.authentificationByIdAndRole(null);
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
        }
    
        return resultat;
    }
    @GetMapping("categories")
    public Bag ListCategoriesVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        int status = 0;
        try {
            connection = PgConnection.connect();
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
    public Bag ListeMarquesVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
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
    public Bag ListeTypesCarburantsVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
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
    public Bag ListeTransmissionsVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
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
    public Bag ListeFreinageVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
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
    public Bag ListeEquipemenstInternesVoiture(Model model) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
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
    public Map<String, Object> createCategorie(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
    
        try {
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");

            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            categorieVoiture.create(ConnectionPostgres.connectDefault());
    
           
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
        }
    
        return resultat;
    }
    // ---------------- MODIFIER -------------------------------
    @PutMapping("catego/{id}")
    public Map<String, Object> updateCategorie(@PathVariable String id,@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur

        try {
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");

            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            categorieVoiture.update(ConnectionPostgres.connectDefault());
    
           
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
        }
    
        return resultat;
    }

    @PutMapping("categorie/{id}")
    public Map putMethodName(@PathVariable String id,@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur

        try {
            String name = (String) requestBody.get("nom");
            String description = (String) requestBody.get("description");

            donnes.add(name);
            donnes.add(description);
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.setNom(name);
            categorieVoiture.setDescription(description);
            System.out.println(id+" "+name+" "+description);
            categorieVoiture.update(ConnectionPostgres.connectDefault());
    
           
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
        }
    
        return resultat;
    }

    @DeleteMapping("categorie/{id}")
    public Map deletcategorie(@PathVariable String id) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> data = new HashMap<>();
        Vector<String> donnes = new Vector<>();
        //status = 200; // Modification réussie
        //status = 400; // Mauvaise requête
        // status = 500; // Erreur interne du serveur

        try {

            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(id);
            categorieVoiture.delete(ConnectionPostgres.connectDefault());
    
           
            status = 200;
            titre = "Suppression de categorie effectue";
            message = "Bravo , vous avez supprimer une categorie de voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Modification de categorie a échoué";
            message = e.getMessage();
        } finally {
            resultat.put("data", donnes);
            resultat.put("status", status);
                resultat.put("titre", titre);
                resultat.put("message", message);
        }
    
        return resultat;
    }
}
