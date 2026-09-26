import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/placement_analyzer";

    private static final String USER = "Enter your user name";

    private static final String PASSWORD = "Enter your password";

    public static Connection getConnection() {

        try {
            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Database connected successfully!");

            return con;

        } catch (Exception e) {

            System.out.println("Database connection failed.");
            e.printStackTrace();

            return null;
        }
    }
}