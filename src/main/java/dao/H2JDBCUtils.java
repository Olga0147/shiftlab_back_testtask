package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/***
 * Класс отвечает за подключение к базе данных
 */
public class H2JDBCUtils {
    private static String jdbcURL = "jdbc:h2:~/test";
    private static String jdbcUsername = "admin";
    private static String jdbcPassword = "admin";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
