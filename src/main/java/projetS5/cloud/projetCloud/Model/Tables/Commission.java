package projetS5.cloud.projetCloud.Model.Tables;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import projetS5.cloud.projetCloud.Model.DatabaseConnection.ConnectionPostgres;

public class Commission {
    private String id;
    private double prixMin;
    private double prixMax;
    private double tauxCommission;
    private Date date;

    // Constructeur, getters, setters

    public Commission() {
        // Constructeur par défaut
    }

    public Commission(double prixMin, double prixMax, double tauxCommission, Date date) {
        this.prixMin = prixMin;
        this.prixMax = prixMax;
        this.tauxCommission = tauxCommission;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(double prixMin) {
        this.prixMin = prixMin;
    }

    public double getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(double prixMax) {
        this.prixMax = prixMax;
    }

    public double getTauxCommission() {
        return tauxCommission;
    }

    public void setTauxCommission(double tauxCommission) {
        this.tauxCommission = tauxCommission;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Insérer une nouvelle commission dans la base de données
    public void insertNewCommission(Connection connection) throws Exception {
        String query = "INSERT INTO commission (prix_min, prix_max, taux_commission, date) VALUES (?, ?, ?, ?);";
        PreparedStatement statement = null;
        boolean statementOpen = false;
        boolean closeable = false;

        try {
            if (connection == null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
                closeable = true;
            }

            statement = connection.prepareStatement(query);
            statement.setDouble(1, this.getPrixMin());
            statement.setDouble(2, this.getPrixMax());
            statement.setDouble(3, this.getTauxCommission());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            statementOpen = true;
            statement.executeUpdate();
            statement.close();

        } catch (Exception e) {
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (closeable) {
                connection.commit();
                connection.close();
            }
        }
    }

    public static Commission[] getAllCommissions(Connection connection) throws Exception {
        String query = "SELECT * FROM commission";
        List<Commission> commissions = new ArrayList<>();
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
            statementOpen = true;
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Commission commission = new Commission();
                commission.setId(resultSet.getString("id"));
                commission.setPrixMin(resultSet.getDouble("prix_min"));
                commission.setPrixMax(resultSet.getDouble("prix_max"));
                commission.setTauxCommission(resultSet.getDouble("taux_commission"));
                commission.setDate(resultSet.getDate("date"));

                commissions.add(commission);
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

        return commissions.toArray(new Commission[0]);
    }

    // Récupérer la commission avec le prix donné dans la plage [prix_min, prix_max]
    public static Commission getCommissionForPrice(double prix,Connection connection) throws Exception {
        String query = "SELECT * FROM commission WHERE ? BETWEEN prix_min AND prix_max ORDER BY date DESC LIMIT 1;";
        Commission commission = new Commission(0, 0, 0, null);
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
            statement.setDouble(1, prix);

            statementOpen = true;
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                commission.setId(resultSet.getString("id"));
                commission.setPrixMin(resultSet.getDouble("prix_min"));
                commission.setPrixMax(resultSet.getDouble("prix_max"));
                commission.setTauxCommission(resultSet.getDouble("taux_commission"));
                commission.setDate(resultSet.getDate("date"));
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

        return commission;
    }

    public void update(Connection connection) throws Exception {
        String sql = "UPDATE commission SET prix_min = ?, prix_max = ?, taux_commission = ?, date = ? WHERE id = ?";
        try (PreparedStatement prstmt = connection.prepareStatement(sql)) {
            if (connection == null) {
                connection = ConnectionPostgres.connectDefault();
                connection.setAutoCommit(false);
            }
            prstmt.setDouble(1, getPrixMin());
            prstmt.setDouble(2, getPrixMax());
            prstmt.setDouble(3, getTauxCommission());
            prstmt.setDate(4, getDate());
            prstmt.setString(5, getId());

            prstmt.executeUpdate();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            if (!connection.isClosed() && connection != null) {
                connection.isClosed();
            }
        }
    }
}

