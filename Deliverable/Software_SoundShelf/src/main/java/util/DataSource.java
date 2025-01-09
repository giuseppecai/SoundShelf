package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

    private static DataSource instance;
    private String url;
    private String username;
    private String password;
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL driver", e);
        }
    }

    private DataSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DataSource getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DataSource is not initialized. Call init() first.");
        }
        return instance;
    }

    public static void init(String url, String username, String password) {
        if (instance == null) {
            instance = new DataSource(url, username, password);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

