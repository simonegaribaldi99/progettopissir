package pissir2025.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseManager {
	private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/samis/Desktop/pissir2025/src/main/java/pissir2025/database/Database.db";
    private static Connection connection = null;

    public static void init() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("Connessione al database stabilita.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        return connection;
    }

}
