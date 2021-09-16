import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertRowTest extends ServerConnector {


    @Test
    public static void SetRows() throws SQLException, IOException {
        try {

            String sql = "INSERT INTO students(ID,firstName,lastName,phone) " + "VALUES (?,?,?,?)";
            PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, 1004);
            pstmt.setString(2, "George");
            pstmt.setString(3, "Hamilton");
            pstmt.setString(4, "5558578965");


//            ResultSetMetaData meta =pstmt.getMetaData();
//            System.out.println("number of rows: "+meta.getColumnCount());
            int rowAffected = pstmt.executeUpdate();
            System.out.println(rowAffected);
//            if (rowAffected == 1) {

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            rs.next();
            int ID = rs.getInt(1);
            System.out.println(ID);
            String firstName = rs.getString(2);
            System.out.println(firstName);

            pstmt.getConnection().commit();
//            }
//        else {
//                System.out.println("Bad executeUpdate");
//            }


        } catch (Exception e) {
            e.printStackTrace();


        }


    }
}


