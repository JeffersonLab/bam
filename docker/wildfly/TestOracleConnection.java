import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestOracleConnection {
    public static void testConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.OracleDriver");

        try (
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@oracle:1521/xepdb1", "BAM_OWNER", "password");
                Statement stmt = con.createStatement();
        ){
            ResultSet rs = stmt.executeQuery("select 1 from dual");
        }
    }


    public static void main(String[] args) {
        try {
            testConnection();
        } catch(Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
    }
}