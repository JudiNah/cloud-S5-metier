package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;

public class CategorieVoiture {
    String id;
    String nom;
    String description;

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

    public void create(Connection connection) throws SQLException {
        String sql = "INSERT INTO categorie_voiture(nom, description) VALUES (?, ?)";
        PreparedStatement prtmt = connection.prepareStatement(sql);
        prtmt.setString(1, getNom());
        prtmt.setString(2, getDescription());
        prtmt.executeUpdate();
    }

    public List<CategorieVoiture> read(Connection connection) throws Exception {
        List<CategorieVoiture> categorieVoitureList = new ArrayList<>();
        
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT *FROM categorie_voiture");
        while (rs.next()) {
            CategorieVoiture categorieVoiture = new CategorieVoiture();
            categorieVoiture.setId(rs.getString("id"));
            categorieVoiture.setNom(rs.getString("nom"));
            categorieVoiture.setDescription(rs.getString("description"));
            categorieVoitureList.add(categorieVoiture);
        }
        return categorieVoitureList;
    }

    public void update(Connection connection) throws Exception {
        String sql = "UPDATE categorie_voiture SET nom = ? , description = ? WHERE id =?";
        PreparedStatement prstmt = connection.prepareStatement(sql);
        prstmt.setString(1, getNom());
        prstmt.setString(2, getDescription());
        prstmt.setString(3, getId());

        prstmt.executeUpdate();
    }

    public void delete(Connection connection) throws Exception {
        String sql = "delete from categorie_voiture WHERE id = ?";
        PreparedStatement prstmt = connection.prepareStatement(sql);
        prstmt.setString(1, getId());
        prstmt.executeUpdate();
    }
}
