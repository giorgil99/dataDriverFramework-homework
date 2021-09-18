import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.*;

public class TestWithSQL extends ServerConnector{


    public static int gID;
    public static String gFirstName;
    public static String gLastName;
    public static String gPhone;


   public static String allRowsQuery = "SELECT firstName, lastName, phone " + "FROM students";


   @BeforeTest
// beforeTest to count rows before any changes are made
   public  void  checkRows() throws SQLException, IOException {
       int rowCountBeforeCommit = RowCounter.connectToCount();
       System.out.println("Row count before INSERT: " +rowCountBeforeCommit );


   }

    @Test
    public  void mainSQLTest() throws SQLException, IOException {
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

            // updates and checks row update status
            int rowAffected = pstmt.executeUpdate();
            System.out.println("Updated rows count: " + rowAffected);


            // using GeneratedKeys method
            ResultSet rsk = pstmt.getGeneratedKeys();

            if (rsk.next()) {

                System.out.println("New row number " + rsk.getInt(1));

            }

//           committing and closing connection



            pstmt.getConnection().commit();
            pstmt.close();


            int rowCountAfterCommit = RowCounter.connectToCount();
            System.out.println("Row count after commit: " +rowCountAfterCommit );



    // creating statement connection as prepareStatement does not allow querying
            Statement pstmt1 = getConnection().createStatement();


//            takes last row

            String checker = "SELECT TOP 1 * FROM students ORDER BY ID DESC";
            ResultSet rs = pstmt1.executeQuery(checker);


            if (rs.next()) {
                gID = rs.getInt(1);
                gFirstName = rs.getString("firstName");
                gLastName = rs.getString("lastName");
                gPhone = rs.getString("phone");
                System.out.println(">>> " + gID + " " + gFirstName + " " + gLastName + " " + gPhone);
//                basic assertions
                Assert.assertEquals(gID, 1004);
                Assert.assertEquals(gFirstName, "George");
                Assert.assertEquals(gLastName, "Hamilton");
                Assert.assertEquals(gPhone, "5558578965");

            }
//       getting row with set id=1004 to update it
            String updateQuery = "update students set firstName = 'Lewis' where id = 1004";
            pstmt1.executeUpdate(updateQuery);

//             checking that update is set

            String bringData = "SELECT firstName " + "FROM students " + "WHERE ID=1004";
            ResultSet rs1 = pstmt1.executeQuery(bringData);
            rs1.next();
            gFirstName = rs1.getString("firstName");
            System.out.println("new name is: " + gFirstName);
            Assert.assertEquals(gFirstName, "Lewis");
            pstmt1.getConnection().commit();

// this class need 1004 ID to be vacant else it throws an exception. delete 1004 row from 'students' table




        } catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
            if (pstmt != null) {
                pstmt.getConnection().rollback();
                System.out.println("Exception !!!");
            }


        }
    }


}