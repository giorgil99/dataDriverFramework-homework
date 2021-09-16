import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.*;

public class TestWithSQL extends ServerConnector {


    public static int gID;
    public static String gFirstName;
    public static String gLastName;
    public static String gPhone;


    @Test
    public static void mainSQLTest() throws SQLException, IOException {
        PreparedStatement pstmt = null;

//       Use autosaved false mode  - this setting is handled by ServerConnector class

        try {

//             Insert in database students new row
            String sql = "INSERT INTO students(ID,firstName,lastName,phone) " + "VALUES (?,?,?,?)";
            pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, 1004);
            pstmt.setString(2, "George");
            pstmt.setString(3, "Hamilton");
            pstmt.setString(4, "5558578965");

            int rowAffected = pstmt.executeUpdate();
            System.out.println("Updated rows count: " + rowAffected);

            ResultSet rsk = pstmt.getGeneratedKeys();

            if (rsk.next()) {
                System.out.println("new row number " + rsk.getInt(1));
                pstmt.getConnection().commit();
                pstmt.close();
            }


            Statement pstmt1 = getConnection().createStatement();
            String allrowsquery = "SELECT firstName, lastName, phone " + "FROM students";
            ResultSet endRowCounter = pstmt1.executeQuery(allrowsquery);
            ResultSetMetaData metaData = endRowCounter.getMetaData();
            int rowNumber = metaData.getColumnCount();
            System.out.println("row number is still: " + rowNumber);


            String checker = "SELECT TOP 1 * FROM students ORDER BY ID DESC";
            ResultSet rs = pstmt1.executeQuery(checker);


            if (rs.next()) {
                gID = rs.getInt(1);
                gFirstName = rs.getString("firstName");
                gLastName = rs.getString("lastName");
                gPhone = rs.getString("phone");
                System.out.println(">>> " + gID + " " + gFirstName + " " + gLastName + " " + gPhone);

                Assert.assertEquals(gID, 1004);
                Assert.assertEquals(gFirstName, "George");
                Assert.assertEquals(gLastName, "Hamilton");
                Assert.assertEquals(gPhone, "5558578965");

            }

//            String updater = "UPDATE students " +"SET firstName='Lewis' " + "WHERE ID = 1004 ";
            String updateQuery = "update students set firstName = 'Lewis' where id = 1004";
            pstmt1.executeUpdate(updateQuery);

//             pstmt0.executeQuery(updater);
            String bringData = "SELECT firstName " + "FROM students " + "WHERE ID=1004";
            ResultSet rs1 = pstmt1.executeQuery(bringData);
            rs1.next();
            gFirstName = rs1.getString("firstName");
            System.out.println("new name is: " + gFirstName);
            Assert.assertEquals(gFirstName, "Lewis");


        } catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
            if (pstmt != null) {
                pstmt.getConnection().rollback();
                System.out.println("Exception !!!");
            }


        }
    }


}