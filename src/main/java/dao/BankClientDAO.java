package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        boolean validClient;

        try (PreparedStatement statement = connection.prepareStatement("select id from bank_client where name = ? and password = ?")) {
            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            validClient = rs.next();
        }

        return validClient;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update bank_client set money = money + ? where name = ? and password = ?")) {
            statement.setLong(1, transactValue);
            statement.setString(2, name);
            statement.setString(3, password);

            statement.executeUpdate();
        }

    }

    public void updateClientsMoney(String name, Long transactValue) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update bank_client set money = money + ? where name = ?")) {
            statement.setLong(1, transactValue);
            statement.setString(2, name);

            statement.executeUpdate();
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        boolean validSum;

        try (PreparedStatement statement = connection.prepareStatement("select id from bank_client where name = ? and money >= ?")) {
            statement.setString(1, name);
            statement.setLong(2, expectedSum);

            ResultSet rs = statement.executeQuery();
            validSum = rs.next();
        }

        return validSum;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> list = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("select * from bank_client")) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                BankClient bankClient = new BankClient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getLong("money")
                );

                list.add(bankClient);
            }
        }

        return list;
    }

    public BankClient getClient(String name, String password) throws SQLException {
        BankClient client = null;

        try (PreparedStatement statement = connection.prepareStatement("select * from bank_client where name = ? and password = ?")) {
            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                client = new BankClient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getLong("money")
                );
            }
        }

        return client;
    }

    public BankClient getClientById(long id) throws SQLException {
        BankClient bankClient = null;

        try (PreparedStatement statement = connection.prepareStatement("select * from bank_client where id = ? limit 1")) {
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bankClient = new BankClient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getLong("money")
                );
            }
        }

        return bankClient;
    }

    public long getClientIdByName(String name) throws SQLException {
        long id = -1;

        try (PreparedStatement statement = connection.prepareStatement("select id from bank_client where name = ?")) {
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }
        }

        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        BankClient bankClient = null;

        try (PreparedStatement statement = connection.prepareStatement("select * from bank_client where name = ?")) {
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bankClient = new BankClient(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getLong("money")
                );
            }
        }

        return bankClient;
    }

    public boolean addClient(BankClient client) throws SQLException {
        boolean added;

        try (PreparedStatement statement = connection.prepareStatement("insert ignore into bank_client (name, password, money) VALUES (?, ?, ?)")) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getPassword());
            statement.setLong(3, client.getMoney());

            added = statement.executeUpdate() == 1;
        }

        return added;
    }

    public boolean deleteClient(String name) throws SQLException {
        boolean removed;

        try (PreparedStatement statement = connection.prepareStatement("delete from bank_client where name = ?")) {
            statement.setString(1, name);

            removed = statement.executeUpdate() == 1;
        }

        return removed;
    }

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id), unique key (name))");
        statement.close();
    }

    public void dropTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS bank_client");
        statement.close();
    }
}
