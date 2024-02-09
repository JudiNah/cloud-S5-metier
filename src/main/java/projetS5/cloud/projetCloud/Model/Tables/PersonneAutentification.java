package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;

public class PersonneAutentification extends Personne{
    String id;
    String email;
    String motPasse;
    Boolean isAdmin;
    String personneId;
    Personne personne;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getPersonneId() {
        return personneId;
    }

    public void setPersonneId(String personneId) {
        this.personneId = personneId;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public PersonneAutentification(String id) {
        super(id);
    }

    public PersonneAutentification (String id,String email, String motPasse, Boolean isAdmin, Personne personne) {
        super();
        this.setId(id);
        this.setEmail(email);
        this.setMotPasse(motPasse);
        this.setAdmin(isAdmin);
        this.setPersonne(personne);
    }
    public PersonneAutentification(){}

    public List<PersonneAutentification> getUtilisateurs (Connection connection) throws SQLException {
        List<PersonneAutentification> prsauths = new ArrayList<>();
        String sql = "SELECT *FROM personne_autentification WHERE is_admin = ?";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)) {
            prstmt.setBoolean(1, getAdmin());
            try (ResultSet rs = prstmt.executeQuery()){
                while (rs.next()) {
                    PersonneAutentification prauth = new PersonneAutentification(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("mot_passe"),
                        rs.getBoolean("is_admin"),
                        new Personne().getPersonne(connection, rs.getString("personne_id"))
                    );
                    prsauths.add(prauth);
                }
            }
        }
        return prsauths;
    }

    public boolean authentificationAdminVerification(Connection connection) throws Exception{
        String query = "select * from personne_autentification where is_admin='t' and email=? and mot_passe=?";
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
            statement.setString(1, this.getEmail());
            statement.setString(2, this.getMotPasse());

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
    public String getIdAdminByEmailAndPassword(Connection connection) throws Exception{
        String query = "select id from personne_autentification where is_admin='t' and email=? and mot_passe=?";
        String id = "";
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
            statement.setString(1, this.getEmail());
            statement.setString(2, this.getMotPasse());

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
               id = resultSet.getString("id");
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

        return id;
    }

    
    public boolean authentificationClientVerification(Connection connection) throws Exception{
        String query = "select * from personne_autentification where is_admin='f' and email=? and mot_passe=?";
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
            statement.setString(1, this.getEmail());
            statement.setString(2, this.getMotPasse());

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
    public String getIdClientByEmailAndPassword(Connection connection) throws Exception{
        String query = "select id from personne_autentification where is_admin='f' and email=? and mot_passe=?";
        String id = "";
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
            statement.setString(1, this.getEmail());
            statement.setString(2, this.getMotPasse());

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
               id = resultSet.getString("id");
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

        return id;
    }

    public String create(Connection connection) throws Exception{
        String query = "INSERT INTO personne_autentification (email, mot_passe, is_admin, personne_id) VALUES ( ?, ?, ?, ? );";
        String id = "";
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
            statement.setString(1, this.getEmail());
            statement.setString(2, this.getMotPasse());
            statement.setBoolean(3, this.getAdmin());
            statement.setString(4,this.getPersonneId());

            statementOpen = true;
            statement.executeUpdate();

         

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

        return id;
    }

    public void delete(Connection connection) throws SQLException {

    }

    
    public String authentificationByIdAndRole(Connection connection) throws Exception{
        String query = "select * from personne_autentification where id=? and is_admin=?";
        if (this.getAdmin()==null) {
             query = "select * from personne_autentification where id=?";
        }
        String id = "";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean statementOpen = false;
        boolean resultSetOpen = false;
        boolean closeable = false;
        boolean exist = false;
        PersonneAutentification prauth = null;
        try {
            if (connection == null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                closeable = true;
            }

            statement = connection.prepareStatement(query);
            statement.setString(1, this.getId());
            String isAdminString = "f";
            if (this.getAdmin()!=null) {

            if (this.getAdmin()) {
                isAdminString = "t";
            }
            statement.setBoolean(2, this.getAdmin());  }

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prauth = new PersonneAutentification(
                resultSet.getString("id"),
                resultSet.getString("email"),
                resultSet.getString("mot_passe"),
                resultSet.getBoolean("is_admin"),
                new Personne().getPersonne(connection, resultSet.getString("personne_id"))
            );
            exist = true;
            }
            if (!exist) {
                throw new Exception("Personne invalide");
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

        return id;
    }


    public PersonneAutentification getAuthentificationPersonneById(Connection connection) throws Exception{
        String query = "select * from personne_autentification where id=? and is_admin=?";
        
        String id = "";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        boolean statementOpen = false;
        boolean resultSetOpen = false;
        boolean closeable = false;
        boolean exist = false;
        PersonneAutentification prauth = null;
        try {
            if (connection == null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                closeable = true;
            }

            statement = connection.prepareStatement(query);
            statement.setString(1, this.getId());
            statement.setBoolean(2, this.getAdmin());  

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                prauth = new PersonneAutentification(
                resultSet.getString("id"),
                resultSet.getString("email"),
                resultSet.getString("mot_passe"),
                resultSet.getBoolean("is_admin"),
                new Personne().getPersonne(connection, resultSet.getString("personne_id"))
            );
            exist = true;
            }
            if (!exist) {
                throw new Exception("Personne invalide");
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

        return prauth;
    }

}
