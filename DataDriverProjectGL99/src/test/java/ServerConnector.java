import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ServerConnector {


    //sets connection to server

    public static Connection getConnection() throws SQLException, IOException {
        Connection con = null;
        try (FileInputStream f = new FileInputStream("src/test/resources/db.properties")) {

            Properties pros = new Properties();

            pros.load(f);

            String url =pros.getProperty("url");
            String user =pros.getProperty("user");
            String password =pros.getProperty("password");


            // main line for connection to server
            con= DriverManager.getConnection(url,user,password);
//            Use autosaved false mode
            con.setAutoCommit(false);
            System.out.println("connection established");


        } catch (IOException e) {
            System.out.println("wrong con");
            System.out.println(e.getMessage());

        }


        return con ;
    }
}
