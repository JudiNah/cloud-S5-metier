package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MarqueVoiture {
    String id;
    String nom;
    String description;
    Date dateCreation;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void create(Connection connection) throws SQLException {
        String sql = "INSERT INTO marque_voiture(nom, description, date_creation) VALUES (?, ?, ?)";
        PreparedStatement prtmt = connection.prepareStatement(sql);
        prtmt.setString(1, getNom());
        prtmt.setString(2, getDescription());
        prtmt.setDate(3, getDateCreation());
        prtmt.executeUpdate();
    }

    public List<MarqueVoiture> read(Connection connection) throws SQLException {
        List<MarqueVoiture> marqueVoitureList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT *FROM marque_voiture");
        while (rs.next()) {
            MarqueVoiture marqueVoiture = new MarqueVoiture();
            marqueVoiture.setId(rs.getString("id"));
            marqueVoiture.setNom(rs.getString("nom"));
            marqueVoiture.setDescription(rs.getString("description"));
            marqueVoiture.setDateCreation(rs.getDate("date_creation"));
            marqueVoitureList.add(marqueVoiture);
        }
        return marqueVoitureList;
    }

    public void update(Connection connection) throws SQLException {
        String sql = "UPDATE marque_voiture SET nom = ? , description = ? , date_creation = ? WHERE id =?";
        PreparedStatement prstmt = connection.prepareStatement(sql);
        prstmt.setString(1, getNom());
        prstmt.setString(2, getDescription());
        prstmt.setDate(3, getDateCreation());
        prstmt.setString(4, getId());

        prstmt.executeUpdate();
    }

    public void delete(Connection connection) throws Exception {
        String sql = "DELETE FROM marque_voiture WHERE id = ?";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)){
            prstmt.setString(1, getId());
            prstmt.executeUpdate();
        }
    }
}
