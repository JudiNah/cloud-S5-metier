package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementInterne {

    String id;
    String nom;


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

    public List<EquipementInterne> getEquipeInternesByVoitureId(Connection connection, String voitureId) throws  Exception {
        List<EquipementInterne> equipementInterneList = new ArrayList<>();
        String sql = "SELECT ei.id, ei.nom FROM equipement_interne ei "+
                " JOIN voiture_equipement_interne vei ON vei.equipement_interne_id = ei.id" +
                " WHERE voiture_id = "+voitureId;
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            EquipementInterne equipementInterne = new EquipementInterne();
            equipementInterne.setId(rs.getString("id"));
            equipementInterne.setNom(rs.getString("nom"));
            equipementInterneList.add(equipementInterne);
        }
        return equipementInterneList;
    }

    public void create(Connection connection) throws SQLException {
        String sql = "INSERT INTO equipement_interne(nom) VALUES (?)";
        PreparedStatement prtmt = connection.prepareStatement(sql);
        prtmt.setString(1, getNom());
        prtmt.executeUpdate();
    }

    public List<EquipementInterne> read(Connection connection) throws SQLException {
        List<EquipementInterne> equipementInterneList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT *FROM equipement_interne");
        while (rs.next()) {
            EquipementInterne equipementInterne = new EquipementInterne();
            equipementInterne.setId(rs.getString("id"));
            equipementInterne.setNom(rs.getString("nom"));
            equipementInterneList.add(equipementInterne);
        }
        return equipementInterneList;
    }

    public void update(Connection connection) throws SQLException {
        String sql = "UPDATE equipement_interne SET nom = ?  WHERE id =?";
        PreparedStatement prstmt = connection.prepareStatement(sql);
        prstmt.setString(1, getNom());
        prstmt.setString(2, getId());

        prstmt.executeUpdate();
    }

    public void delete(Connection connection) throws SQLException {
        String sql = "delete from  equipement_interne WHERE id =?";
        PreparedStatement prstmt = connection.prepareStatement(sql);
        prstmt.setString(1, getId());

        prstmt.executeUpdate();
    }

    /*POUR LE TABLE voiture_equipemeny_interne */
    public void createVEI(Connection connection, String voitureId, String equipementInterneId) throws SQLException {
        String sql = "INSERT INTO voiture_equipement_interne(voiture_id, equipement_interne_id) VALUES (?, ?)";
        PreparedStatement prtmt = connection.prepareStatement(sql);
        prtmt.setString(1, voitureId);
        prtmt.setString(2, equipementInterneId);
        prtmt.executeUpdate();
    }

    public void deleteVEI(Connection connection,  String voitureId, String equipementInterneId) throws SQLException {
        String sql = "DELETE FROM voiture_equipement_interne WHERE voiture_id = ? AND equipement_interne_id = ?";
        PreparedStatement prtmt = connection.prepareStatement(sql);
        prtmt.setString(1, voitureId);
        prtmt.setString(2, equipementInterneId);

        prtmt.executeUpdate();
    }

}
