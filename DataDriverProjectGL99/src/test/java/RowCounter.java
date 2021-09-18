import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.*;

public class RowCounter {

    public static String allRowsQuery = "SELECT COUNT(DISTINCT ID) " + "FROM students";
//   private static String url ="jdbc:sqlserver://localhost:1433;database=TestData";
//   private static String user ="rowchecker";
//   private static String password ="12345";


    public static int connectToCount() throws SQLException, IOException {
//           Connection newConnector =DriverManager.getConnection(url,user,password);
        Connection newConnector =ServerConnector.getConnection();
        Statement checkRowsConn = newConnector.createStatement();
        ResultSet checkRowsRS = checkRowsConn.executeQuery(allRowsQuery);
        checkRowsRS.next();

        return checkRowsRS.getInt(1);


    }


}
