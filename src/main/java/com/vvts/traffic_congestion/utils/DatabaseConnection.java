package com.vvts.traffic_congestion.utils;

import java.sql.*;

/**
 * @auther kul.paudel
 * @created at 2024-08-06
 */
public class DatabaseConnection {
    private static String jdbcDriver = "";
    private static String dbURL = "";
    private static String username = "";
    private static String password = "";
    private Connection connection;

    // set database connection
    public DatabaseConnection() throws SQLException, ClassNotFoundException {
        jdbcDriver = "com.mysql.jdbc.Driver";
        dbURL = "jdbc:mysql://localhost:3306/trafficforecast";
        username = "root";
        password = "root";
        Class.forName(jdbcDriver); //set Java database connectivity driver
        connection = DriverManager.getConnection(dbURL, username, password);

    }
    // execute query for create connection
    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query);
        return st.executeQuery();
    }

    // query for update
    public int executeUpdate(String statement) throws SQLException {
        PreparedStatement st = connection.prepareStatement(statement);

        return st.executeUpdate();
    }

    public void close() {
        try {
            // connection close
            connection.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            connection = null;
        }
    }

    protected void finalize() {
        close();
    }
}
