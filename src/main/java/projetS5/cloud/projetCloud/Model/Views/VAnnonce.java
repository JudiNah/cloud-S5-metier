package projetS5.cloud.projetCloud.Model.Views;


import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projetS5.cloud.projetCloud.Model.Tables.Annonce;
import projetS5.cloud.projetCloud.Model.Tables.AnnonceValidee;
import projetS5.cloud.projetCloud.Model.Tables.Personne;
import projetS5.cloud.projetCloud.Model.Tables.PersonneAutentification;
import projetS5.cloud.projetCloud.Model.Tables.VoiturePrix;

public class VAnnonce {
    Annonce annonce;
    AnnonceValidee annonceValidee;
    CatalogVoiture catalogVoiture;
    VoiturePrix voiturePrix;
    Personne personneClient;
    PersonneAutentification personneAuthUtilisateur;

    public Annonce getAnnonce() {
        return annonce;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public AnnonceValidee getAnnonceValidee() {
        return annonceValidee;
    }

    public void setAnnonceValidee(AnnonceValidee annonceValidee) {
        this.annonceValidee = annonceValidee;
    }

    public CatalogVoiture getCatalogVoiture() {
        return catalogVoiture;
    }

    public void setCatalogVoiture(CatalogVoiture catalogVoiture) {
        this.catalogVoiture = catalogVoiture;
    }

    public VoiturePrix getVoiturePrix() {
        return voiturePrix;
    }

    public void setVoiturePrix(VoiturePrix voiturePrix) {
        this.voiturePrix = voiturePrix;
    }

    public Personne getPersonneClient() {
        return personneClient;
    }

    public void setPersonneClient(Personne personneClient) {
        this.personneClient = personneClient;
    }

    public PersonneAutentification getPersonneAuthUtilisateur() {
        return personneAuthUtilisateur;
    }

    public void setPersonneAuthUtilisateur(PersonneAutentification personneAuthUtilisateur) {
        this.personneAuthUtilisateur = personneAuthUtilisateur;
    }

    public VAnnonce(Annonce annonce,AnnonceValidee annonceValidee, CatalogVoiture catalogVoiture, VoiturePrix voiturePrix, Personne personneClient, PersonneAutentification personneAuthUtilisateur) {
        this.annonceValidee = annonceValidee;
        this.catalogVoiture = catalogVoiture;
        this.voiturePrix = voiturePrix;
        this.personneClient = personneClient;
        this.personneAuthUtilisateur = personneAuthUtilisateur;
        this.annonce = annonce;
    }

    public VAnnonce () {

    }

    public List<VAnnonce> getAnnoncesValideesBetweenPrices( List<VAnnonce> vAnnonces, double prixMin, double prixMax) throws SQLException {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        for (VAnnonce vAnnonce : vAnnonces) {
            double prixVoiture = vAnnonce.getVoiturePrix().getPrix();
            if (prixMin <= prixVoiture && prixVoiture <= prixMax) {
                annonceValideeList.add(vAnnonce);
            }
        }
        return annonceValideeList;
    }

    public List<VAnnonce> getAnnoncesValidees(Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        String sql = "SELECT * FROM v_annonce WHERE date_validation is not null and date_fin is null ";
        try (Statement prstmt = connection.createStatement()) {

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }

    public List<VAnnonce> getAnnoncesValideesMoinChere(Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        String sql = "SELECT * FROM v_annonce WHERE date_validation is not null and date_fin is null order by prix asc";
        try (Statement prstmt = connection.createStatement()) {

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }

    public List<VAnnonce> getAnnoncesValideesRecent(Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        String sql = "SELECT * FROM v_annonce WHERE date_validation is not null and date_fin is null order by date_validation desc";
        try (Statement prstmt = connection.createStatement()) {

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }

    public List<VAnnonce> searchAnnonceByMotCle(String mot,Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        String sql = "SELECT *\r\n" + //
                "FROM v_annonce\r\n" + //
                "WHERE date_validation IS NOT NULL AND date_fin IS NULL\r\n" + //
                "      AND (\r\n" + //
                "            date_validation::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR nom_client LIKE '%"+mot+"%'\r\n" + //
                "         OR prenom_client LIKE '%"+mot+"%'\r\n" + //
                "         OR address_client LIKE '%"+mot+"%'\r\n" + //
                "         OR utilisateur_id LIKE '%"+mot+"%'\r\n" + //
                "         OR date_debut::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR date_fin::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR annonce_id LIKE '"+mot+"a%'\r\n" + //
                "         OR prix_commission::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR prix::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR voiture_id LIKE '%"+mot+"%'\r\n" + //
                "         OR annee_fabrication::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR couleur LIKE '%"+mot+"%'\r\n" + //
                "         OR consommation::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR nom_categorie LIKE '%"+mot+"%'\r\n" + //
                "         OR description_categorie LIKE '%"+mot+"%'\r\n" + //
                "         OR nom_marque LIKE '%"+mot+"%'\r\n" + //
                "         OR description_marque LIKE '%"+mot+"%'\r\n" + //
                "         OR date_creation_marque::TEXT LIKE '%"+mot+"%'\r\n" + //
                "         OR nom_type_carburant LIKE '%a%'\r\n" + //
                "         OR nom_transmission LIKE '%"+mot+"%'\r\n" + //
                "         OR nom_freignage LIKE '%"+mot+"%'\r\n" + //
                "      );\r\n" + //
                "";
        try (Statement prstmt = connection.createStatement()) {

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }

    
    public List<VAnnonce> getAnnoncesValideesByIdPersonne(String id_client,Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();

        String sql = "SELECT * FROM v_annonce WHERE date_validation is not null and date_fin is null  and utilisateur_id='"+id_client+"'";
        try (Statement prstmt = connection.createStatement()) {
            

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }
    public List<VAnnonce> getAnnoncesNotValidees(Connection connection) throws Exception {
        List<VAnnonce> annonceValideeList = new ArrayList<>();
        String sql = "SELECT * FROM v_annonce WHERE date_validation is null and date_fin is null ";
        try (Statement prstmt = connection.createStatement()) {

            try (ResultSet rs = prstmt.executeQuery(sql)) {
                while (rs.next()) {
                    AnnonceValidee annonceValidee = new AnnonceValidee(rs.getDate("date_validation"));
                    Personne personneClient = new Personne(rs.getString("nom_client"), rs.getString("prenom_client"), rs.getString("address_client"));
                    personneClient.setId(rs.getString("utilisateur_id"));
                    PersonneAutentification utilisateur = new PersonneAutentification(rs.getString("utilisateur_id"));;
                    VoiturePrix voiturePrix = new VoiturePrix(rs.getDouble("prix"));
                    CatalogVoiture catalogVoiture = new CatalogVoiture(rs.getDate("annee_fabrication"), rs.getString("couleur"), rs.getDouble("consommation"), rs.getString("nom_categorie"),rs.getString("description_categorie"), rs.getString("nom_marque"), rs.getString("description_marque"), rs.getDate("date_creation_marque"), rs.getString("nom_type_carburant"), rs.getString("nom_transmission"), rs.getString("nom_freignage"));
                    Annonce annonce = new Annonce();
                    annonce.setAnnonceId(rs.getString("annonce_id"));
                    annonce.setDateDebut(rs.getDate("date_debut"));
                    annonce.setCommission(rs.getDouble("prix_commission"));
                    VAnnonce vAnnonce = new VAnnonce(annonce,annonceValidee, catalogVoiture, voiturePrix, personneClient, personneAuthUtilisateur);
                    vAnnonce.setPersonneAuthUtilisateur(utilisateur);
                    annonceValideeList.add(vAnnonce);
                }
            }
        }
        return annonceValideeList;
    }

}
