package shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

    // Make sure your XAMPP/MySQL server has this exact database created: 'oop2-capstone'
    public static final String URL = "jdbc:mysql://localhost:3306/oop2-capstone";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             System.out.println("Connected Successfully");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public static void main(String[] args) {
        getConnection();
    }
}