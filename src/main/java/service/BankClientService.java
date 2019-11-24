package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public List<BankClient> getAllClient() {
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public BankClient getClient(String name, String password) {
        try {
            return getBankClientDAO().getClient(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientByName(String name) {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addClient(BankClient client) {
        try {
            return getBankClientDAO().addClient(client);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClient(String name) {
        try {
            return getBankClientDAO().deleteClient(name);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) {
        try {
            return getBankClientDAO().isClientHasSum(name, expectedSum);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateClient(String name, String password) {
        try {
            return getBankClientDAO().validateClient(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        try {
            if (getBankClientDAO().validateClient(sender.getName(), sender.getPassword())
                    && getBankClientDAO().isClientHasSum(sender.getName(), value)) {
                if (getBankClientDAO().getClientByName(name) != null) {
                    getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword(), -value);
                    getBankClientDAO().updateClientsMoney(name, value);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            String url = "jdbc:mysql://localhost:3306/db_example?serverTimezone=UTC";
            String user = "root";
            String password = "5070129";

            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
