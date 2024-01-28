package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;

public class AnnonceValidee {
    String annonceValideeId;
    Date dateValidation;
    String annonceId;
    String personneAuthId;

    public String getAnnonceValideeId() {
        return annonceValideeId;
    }

    public void setAnnonceValideeId(String annonceValideeId) {
        this.annonceValideeId = annonceValideeId;
    }

    public Date getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(Date dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(String annonceId) {
        this.annonceId = annonceId;
    }

    public String getPersonneAuthId() {
        return personneAuthId;
    }

    public void setPersonneAuthId(String personneAuthId) {
        this.personneAuthId = personneAuthId;
    }

    public AnnonceValidee(Date dateValidation, String annonceId, String personneAuthId) {
        this.setDateValidation(dateValidation);
        this.setAnnonceId(annonceId);
        this.setPersonneAuthId(personneAuthId);
    }
    public AnnonceValidee(Date dateValidation) {
        this.setDateValidation(dateValidation);
    }

    public AnnonceValidee() {

    }

    public void create(Connection connection) throws Exception {
        String sql = "INSERT INTO annonce_validee(date_validation, annonce_id, personne_autentification_id) VALUES (?, ?, ?)";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)) {
            prstmt.setDate(1, getDateValidation());
            prstmt.setString(2, getAnnonceId());
            prstmt.setString(3, getAnnonceValideeId());

            prstmt.executeUpdate();
        }
    }

    public boolean verifyIsExiste(Connection connection) throws Exception{
        String query = "select * from annonce_validee where annonce_id=?";
        boolean isExist = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean statementOpen = false;
        boolean resultSetOpen = false;
        boolean closeable = false;

        try {
            if (connection == null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                closeable = true;
            }

            statement = connection.prepareStatement(query);
            statement.setString(1, this.getAnnonceId());

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
               isExist = true;
            }

            statement.close();
        } catch (Exception e) {
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultSetOpen) {
                resultSet.close();
            }
            if (closeable) {
                connection.commit();
                connection.close();
            }
        }

        return isExist;
    }


}
