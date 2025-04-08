package connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnection {

    private static final String DB_CONFIG_FILE = "etc/config/db.properties";
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        loadConfig();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading database driver: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    private static void loadConfig() {
        Properties props = new Properties();

        try (InputStream input = MySQLConnection.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE)) {
            props.load(input);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = "";
            driver = props.getProperty("db.driver");

        } catch (IOException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            e.printStackTrace();

            url = "jdbc:mysql://localhost:3306/examen_reseaux";
            user = "root";
            password = "";
            driver = "com.mysql.cj.jdbc.Driver";
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            // e.printStackTrace();
            return false;
        }
    }
}
