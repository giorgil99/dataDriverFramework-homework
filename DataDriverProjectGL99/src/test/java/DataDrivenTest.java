import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DataDrivenTest extends ServerConnector {


    public static Object[][] object;
    public static int tagNumber;


    public static void getData() {
        try (
                Statement cstmt = getConnection().createStatement()) {

            // executes query to get table content
            String sql = "SELECT firstName, lastName, phone " + "FROM students";
            ResultSet rs = cstmt.executeQuery(sql);

            // set 3-3 object that later is going to be used by DataProvider
            // using metadata to get all rows number to make object dimensions dynamic

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            int rowCount = RowCounter.connectToCount();
            System.out.println("Number of rows: " + rowCount);
            System.out.println("Number of columns: " + columnsNumber);
            object = new Object[rowCount][columnsNumber];
            int counter = 0;
            // rs.next starts before first row with data
            while (rs.next()) {


                // adds data to object from server here
                for (int i = 0; i < columnsNumber; i++) {


                    object[counter][i] = rs.getString(i+1);
                }
                // counter moves to next column
                counter++;

            }

        } catch (SQLException | IOException throwable) {
            throwable.printStackTrace();
        }


    }

    // DataProvider invokes get data method and returns  it's object
    @DataProvider(name = "filler")
    public Object[][] getDataFromDataProvider() {
        getData();
        return object;


    }


    @Test(dataProvider = "filler")
//    @Test
    public void dataTest(String firstName, String lastName, String number) {


        Configuration.startMaximized = true;
        Configuration.browser = "Edge";
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").sendKeys(firstName);
        $("#lastName").sendKeys(lastName);
        // just clicking on male to be able to click on submit
        $("div.custom-control.custom-radio.custom-control-inline", 1).click();
        $("#userNumber").sendKeys(number);

        $("#submit").scrollIntoView(true);
        $("#submit").click();

        $("tbody > tr:nth-child(1) > td:nth-child(2)").scrollIntoView(true);
        $("tbody > tr:nth-child(1) > td:nth-child(2)").shouldBe(Condition.text(firstName + " " + lastName));

        System.out.println("#" + (tagNumber + 1) + " " + firstName + " " + lastName + " " + number);
        tagNumber++;

    }
//
}
