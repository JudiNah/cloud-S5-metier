package projetS5.cloud.projetCloud.Model.Views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Statistique {

    int mois;
    int annee;
    int nombre;

    public int getMois() {
        return mois;
    }

    public void setMois(int mois) {
        this.mois = mois;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public Statistique(int mois, int annee, int nombre) {
        this.setMois(mois);
        this.setAnnee(annee);
        this.setNombre(nombre);
    }
    public Statistique() {

    }

    public Statistique[] stateVente(Connection connection, int annee) throws SQLException {
        Statistique[] statistiques = new Statistique[12];
        String sql = "SELECT *FROM nombre_ventes_par_mois(?)";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)){
            prstmt.setInt(1, annee);
            try (ResultSet rs = prstmt.executeQuery()){
                int a = 0;
                while (rs.next()) {
                    statistiques[a] = new Statistique(rs.getInt("mois"), rs.getInt("annee"), rs.getInt("nombre"));
                    a++;
                }
            }
        }
        return statistiques;
    }

    public Statistique[] stateAnnonce(Connection connection, int annee) throws SQLException {
        Statistique[] statistiques = new Statistique[12];
        String sql = "SELECT *FROM nombre_annonces_valides_par_mois(?)";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)){
            prstmt.setInt(1, annee);
            try (ResultSet rs = prstmt.executeQuery()){
                int a = 0;
                while (rs.next()) {
                    statistiques[a] = new Statistique(rs.getInt("mois"), rs.getInt("annee"), rs.getInt("nombre"));
                    a++;
                }
            }
        }
        return statistiques;
    }

    public Statistique[] stateUtilisateurs(Connection connection, int annee) throws SQLException {
        Statistique[] statistiques = new Statistique[12];
        String sql = "SELECT *FROM nombre_utilisateurs_par_mois(?)";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)){
            prstmt.setInt(1, annee);
            try (ResultSet rs = prstmt.executeQuery()){
                int a = 0;
                while (rs.next()) {
                    statistiques[a] = new Statistique(rs.getInt("mois"), rs.getInt("annee"), rs.getInt("nombre"));
                    a++;
                }
            }
        }
        return statistiques;
    }
}

