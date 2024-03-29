package projetS5.cloud.projetCloud.Controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projetS5.cloud.projetCloud.Context.PgConnection;
import projetS5.cloud.projetCloud.Model.Bag;
import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;
import projetS5.cloud.projetCloud.Model.Objects.Client;
import projetS5.cloud.projetCloud.Model.Tables.*;
import projetS5.cloud.projetCloud.Model.Utils.JwtToken;
import projetS5.cloud.projetCloud.Model.Views.VAnnonce;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@RestController
public class AnnonceController {
    @PostMapping("annonce_valide")
    public Map<String, Object> validerAnnonce(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);

            String annonce_id = (String) requestBody.get("id_annonce"); 
            
            CreateAnnonceValidee(connection,idAdmin,annonce_id);
            status = 200;
            titre = "Mise a jour de l'annonce en validation";
            message = "Excellent , vous avez valider un publication";
        } catch (Exception e) {
            status = 500;
            titre = "Validation de l'annonce a echoue";
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

    @PostMapping("achete_voiture")
    public Map<String, Object> acheterVoiture(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idClient = jwtToken.checkBearer(authorizationHeader, "client");
            PersonneAutentification personneAutentification = new PersonneAutentification(idClient);
            personneAutentification.setAdmin(false);
            personneAutentification.authentificationByIdAndRole(connection);

            String annonce_id = (String) requestBody.get("id_annonce"); 
            Date dateNow = new Date(new java.util.Date().getTime());
            Annonce annonce = new Annonce();
            annonce.setAnnonceId(annonce_id);
            annonce.setPersonneAutentificationId(idClient);
            annonce.setDateFin(dateNow);
            annonce.acheterVoiture(connection);
            annonce.addDateFinAnnonce(connection);
            status = 200;
            titre = "Achat de voiture effectue";
            message = "Excellent , vous avez acheter une voiture";
        } catch (Exception e) {
            status = 500;
            titre = "Achat de voiture echoue";
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
    
    @PostMapping("annonce")
    public Map<String, Object> createNewAnnonce(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idClient = jwtToken.checkBearer(authorizationHeader, "client");
            PersonneAutentification personneAutentification = new PersonneAutentification(idClient);
            personneAutentification.setAdmin(false);
            personneAutentification.authentificationByIdAndRole(connection);
            double prix = Double.parseDouble((String) requestBody.get("prix").toString()); 
            String code_annonce = "0000";
            Date annee_fabrication = Date.valueOf((String) requestBody.get("annee_fabrication"));
            String couleur = (String) requestBody.get("couleur");
            double consommation = Double.parseDouble((String) requestBody.get("consommation").toString());
            String categorie_voiture_id = (String) requestBody.get("categorie_voiture_id");
            String marque_voiture_id = (String) requestBody.get("marque_voiture_id");
            String type_carburant_voiture = (String) requestBody.get("type_carburant_voiture");
            String transmission_voiture = (String) requestBody.get("transmission_voiture");
            String freignage_voiture = (String) requestBody.get("freignage_voiture");
            List<String> equipement_interne = (List<String>) requestBody.get("equipement_interne");
            double commission = Double.parseDouble((String) requestBody.get("commission").toString());
            commission = (commission*prix)/100;
            String[] equipement_interne_tab = equipement_interne.toArray(new String[0]);
            create(connection,idClient,commission,prix, code_annonce, annee_fabrication, couleur, consommation, categorie_voiture_id, marque_voiture_id, type_carburant_voiture, transmission_voiture, freignage_voiture, equipement_interne_tab);
            status = 200;
            titre = "Creation de nouvelle annonce a fait avec succees";
            message = "Excellent , annonce creer";
        } catch (Exception e) {
            status = 500;
            titre = "Creation de l'annonce a echoue";
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


    @GetMapping("annonce_valides")
    public Map<String, Object> getAllAnnonceValide() throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {
        
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.getAnnoncesValidees(connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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

    @GetMapping("annonce_valides_recent")
    public Map<String, Object> getAllAnnonceValideRecent() throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {
        
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.getAnnoncesValideesRecent(connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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

    @GetMapping("annonce_valides_moin_chere")
    public Map<String, Object> getAllAnnonceValideMoinChere() throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {
        
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.getAnnoncesValideesMoinChere(connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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

    @GetMapping("annonce_personne_valides/{id_personne}")
    public Map<String, Object> getAllAnnonceValideByIdClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String id_personne) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String id = jwtToken.checkBearer(authorizationHeader, "all");
            PersonneAutentification personneAutentification = new PersonneAutentification(id);
            personneAutentification.setAdmin(null);
            personneAutentification.authentificationByIdAndRole(connection); 

            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.getAnnoncesValideesByIdPersonne(id_personne,connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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

    @GetMapping("annonce_not_valides")
    public Map<String, Object> getAllAnnonceNotValide(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.getAnnoncesNotValidees(connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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

    @GetMapping("annonce/{motcle}")
    public Map<String, Object> rechercheMultiCritere(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String motcle) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        List<VAnnonce> allAnnoncesValides = null;
        Connection connection = null;
        try {

            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "client");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(false);
            personneAutentification.authentificationByIdAndRole(connection);
            VAnnonce annoncesV = new VAnnonce();
            allAnnoncesValides = annoncesV.searchAnnonceByMotCle(motcle,connection);
            status = 200;
            titre = "Prendre tout les validations est fait avec succees";
            message = "Excellent , voici tout les annonces valides";
        } catch (Exception e) {
            status = 500;
            titre = "Prendre les annonces valides a echoue";
            message = e.getMessage();
            e.printStackTrace();
        } finally {
            resultat.put("annoces", allAnnoncesValides);
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
   
    public void create(Connection connection,String perAuth,double commission ,double prix, String code_annonce, Date annee_fabrication, String couleur, double consommation, String categorie_voiture_id, String marque_voiture_id, String type_carburant_voiture_id, String transmission_voiture_id, String freignage_voiture_id , String[] equipement_interne ) throws Exception {
        connection.setAutoCommit(false);

        Voiture voiture = new Voiture(annee_fabrication, couleur, consommation, categorie_voiture_id, marque_voiture_id, type_carburant_voiture_id, transmission_voiture_id, freignage_voiture_id, null);
        String voitureId = voiture.create(connection);//Insertion de voiture

        VoiturePrix voiturePrix = new VoiturePrix(new Date(new java.util.Date().getTime()), prix, voitureId);
        voiturePrix.create(connection);//Insertion prix de voiture

        Date dateDebutAnnonce = new Date(new java.util.Date().getTime());//Date Actuel
        Annonce annonce = new Annonce(dateDebutAnnonce, null, code_annonce, voitureId, perAuth, commission);
        annonce.create(connection);//Insertion de l'annonce

        EquipementInterne ei = new EquipementInterne();
        for (String string : equipement_interne) {
            ei.createVEI(connection, voitureId, string);
        }
        connection.commit();
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

    public void CreateAnnonceValidee(Connection connection ,String personneAuthId,String annonce_id) throws Exception {
        boolean isExist = false;
        try {
            if (connection==null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                isExist = true;
            }
            Date dateValidation = new Date(new java.util.Date().getTime());//date actuel
            AnnonceValidee annonceValidee = new AnnonceValidee(dateValidation, annonce_id, personneAuthId);
            if(annonceValidee.verifyIsExiste(connection)){
                throw new Exception("L'annonce est deja valider");
            }

            annonceValidee.create(connection);
            connection.commit();
        }
        catch (Exception e) {
            connection.rollback();
            throw e;
        }
        finally {
            if (isExist){
                connection.commit();
                connection.close();
            }
        }
    }

    @GetMapping("commission/{price}")
    public Map<String, Object> getCommissionByPrice(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@PathVariable String price) throws Exception{
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Commission commission = null;
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "all");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(null);
            personneAutentification.authentificationByIdAndRole(connection);
            commission = Commission.getCommissionForPrice(Double.parseDouble(price), connection);
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
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }

    @GetMapping("/commissions")
    public Map<String, Object> getAllCommission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();  
        Vector<Commission> response = new Vector<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);
            Commission[] commission = Commission.getAllCommissions(connection);
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
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }
    @PostMapping("/commission")
    public Map<String, Object> createNewCommission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,@RequestBody Map<String, Object> requestBody) throws Exception {
        Map<String, Object> resultat = new HashMap<>();
        int status = 0;
        String titre = null;
        String message = null;
        Map<String, Object> donnes = new HashMap<>();
        Connection connection = null;
        try {
            connection = ConnectionPostgres.connectDefault();
            connection.setAutoCommit(false);
            JwtToken jwtToken = new JwtToken();
            String idAdmin = jwtToken.checkBearer(authorizationHeader, "admin");
            PersonneAutentification personneAutentification = new PersonneAutentification(idAdmin);
            personneAutentification.setAdmin(true);
            personneAutentification.authentificationByIdAndRole(connection);

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
            if (!(connection==null)) {
                connection.commit();
                connection.close();
            }
        }
    
        return resultat;
    }
}
