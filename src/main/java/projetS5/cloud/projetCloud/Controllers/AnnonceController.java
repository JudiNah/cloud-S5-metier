package projetS5.cloud.projetCloud.Controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetS5.cloud.projetCloud.Context.PgConnection;
import projetS5.cloud.projetCloud.Model.Bag;
import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Objects.Client;
import projetS5.cloud.projetCloud.Model.Tables.*;
import projetS5.cloud.projetCloud.Model.Views.VAnnonce;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@RestController
public class AnnonceController {

    @PostMapping("create")
    public Bag create(@RequestBody double prix, @RequestBody String code_annonce, @RequestBody Date date, @RequestBody Date annee_fabrication, @RequestBody String couleur, @RequestBody double consommation, @RequestBody String categorie_voiture_id, @RequestBody String marque_voiture_id, @RequestBody String type_carburant_voiture_id, @RequestBody String transmission_voiture_id, @RequestBody String freignage_voiture_id  ) throws Exception {
        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            connection.setAutoCommit(false);

            Voiture voiture = new Voiture(annee_fabrication, couleur, consommation, categorie_voiture_id, marque_voiture_id, type_carburant_voiture_id, transmission_voiture_id, freignage_voiture_id, null);
            String voitureId = voiture.create(connection);//Insertion de voiture

            VoiturePrix voiturePrix = new VoiturePrix(date, prix, voitureId);
            voiturePrix.create(connection);//Insertion prix de voiture

            Date dateDebutAnnonce = new Date(new java.util.Date().getTime());//Date Actuel
            String perAuth = null;//SESSION
            Annonce annonce = new Annonce(dateDebutAnnonce, null, code_annonce, voitureId, perAuth);
            annonce.create(connection);//Insertion de l'annonce

            connection.commit();
        }
        catch (Exception e) {
            connection.rollback();
            bag = new Bag("Insertion", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @PostMapping("validees-liste")
    public Bag ListeAnnonceValidee() throws Exception {

        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            List<VAnnonce> annoncesValideesList = new VAnnonce().getAnnoncesValidees(connection);
            bag = new Bag(null, null, annoncesValideesList);
        }
        catch (Exception e) {
            connection.rollback();
            bag = new Bag("Insertion", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @PostMapping("validees-liste-par-prix")
    public Bag ListeAnnonceValideeByPrix(@RequestBody double min_prix, @RequestBody double max_prix) throws Exception {

        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();
            List<VAnnonce> annonceValideesList = new VAnnonce().getAnnoncesValidees(connection);
            List<VAnnonce> annoncesValideesList1 = new VAnnonce().getAnnoncesValideesBetweenPrices(annonceValideesList, min_prix, max_prix);
            bag = new Bag(null, null, annoncesValideesList1);
        }
        catch (Exception e) {
            connection.rollback();
            bag = new Bag("Insertion", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;
    }

    @PostMapping("validee-create")
    public Bag CreateAnnonceValidee(@RequestBody String annonce_id) throws Exception {

        Connection connection = null;
        Bag bag = new Bag(null, null, null);
        try {
            connection = PgConnection.connect();

            Date dateValidation = new Date(new java.util.Date().getTime());//date actuel
            String personneAuthId = null;//SESSION
            AnnonceValidee annonceValidee = new AnnonceValidee(dateValidation, annonce_id, personneAuthId);
            annonceValidee.create(connection);
        }
        catch (Exception e) {
            connection.rollback();
            bag = new Bag("Insertion", e.getMessage(), null);
        }
        finally {
            if (!connection.isClosed() && connection != null)
                connection.close();
        }

        return bag;

    }

    @GetMapping("commission/{price}")
    public Map<String, Object> getCommissionByPrice(@PathVariable String price) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Commission commission = null;
        try {
            commission = Commission.getCommissionForPrice(Double.parseDouble(price), null);
           donnes.put("commission", commission);
            status = 200;
            titre = "Prendre le commission est reussi";
            message = "Excellent , voici votre commission par rapport a votre  ";
            
        } catch (Exception e) {
            status = 500;
            titre = "Prendre de l'element necessaire a echoue a echoue";
            message = e.getMessage();
        } finally {
            resultat.put("data", commission);
            resultat.put("status", status);
                resultat.put("titre", titre);
                resultat.put("message", message);
        }
    
        return resultat;
    }

    @GetMapping("/commissions")
    public Map<String, Object> getAllCommission() {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();  
        Vector<Commission> response = new Vector<>();
        try {
            Commission[] commission = Commission.getAllCommissions(null);
            for (Commission commission2 : commission) {
                response.add(commission2);
            }
           donnes.put("commission", response);
            status = 200;
            titre = "Prendre le commission est reussi";
            message = "Excellent , voici tout les liste de commission ";
            System.out.println(commission.length);
        } catch (Exception e) {
            status = 500;
            titre = "Prendre tout les commission a echoue a echoue";
            message = e.getMessage();
        } finally {
            resultat.put("data", response);
            resultat.put("status", status);
                resultat.put("titre", titre);
                resultat.put("message", message);
        }
    
        return resultat;
    }
    @PostMapping("/commission")
    public Map<String, Object> createNewCommission(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
    
        try {
            String prixMin = (String) requestBody.get("prixMin");
            String prixMax = (String) requestBody.get("prixMax");
            String taux = (String) requestBody.get("tauxCommission");
            prixMin = prixMin.trim();
            prixMax = prixMax.trim();
            taux = taux.trim();
            if(prixMin.length()==0 && prixMax.length()==0 && taux.length()==0){
                throw new Exception("Prix minimum , prix Maximim et taux de commission invalide");
            }else
            if (prixMin.length()==0) {
                throw new Exception("prix minimum invalide");
            }else
            if (prixMax.length()==0) {
                throw new Exception("prix maximum invalide");
            }else
            if (taux.length()==0) {
                throw new Exception("taux minimum invalide");
            }
            
            Commission commission = new Commission();
            commission.setPrixMax(Double.parseDouble(prixMax));
            commission.setPrixMin(Double.parseDouble(prixMin));
            commission.setTauxCommission(Double.parseDouble(taux));
            Connection connection = ConnectionPostgres.connectDefault();
            commission.insertNewCommission(connection);
            
            status = 200;
            titre = "Creation de nouvelle commission a fait avec succees";
            message = "Excellent , commission creer";
            donnes.put("prixMinimum", prixMin);
            donnes.put("prixMaximum", prixMax);
            donnes.put("tauxCommission", taux);
            
        } catch (Exception e) {
            status = 500;
            titre = "Creation de commission a echoue";
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
