import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class ExcelDrivenTest {

    public static Object[][] object;
    public static String value ;
    public static int tagNumber;


    public  void  excelData() throws IOException {
//        Path root = Paths.get(".").normalize().toAbsolutePath();
        File file = new File("C:\\Users\\Skylancer T2\\Desktop\\data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);


        object = new Object[4][4];
        int counter =0;
        for (int i = 0; i < 4; i++) {

            Row row = sheet.getRow(i+2);

            for (int k=0; k<4;k++){

            Cell cell = row.getCell(k+2);
            value = cell.getStringCellValue();
            object[counter][k]= value;
        }
            counter++;

    }
        System.out.println(object[1][1]);
}
    @DataProvider(name = "filler")
    public Object[][] getDataFromDataProvider() throws IOException {
        excelData();
        return object;


    }

    @Test(dataProvider = "filler")
//    @Test
    public void dataTest(String firstName, String lastName, String number, String gender) {


        Configuration.startMaximized = true;
        Configuration.browser = "Edge";
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").sendKeys(firstName);
        $("#lastName").sendKeys(lastName);
        // just clicking on male to be able to click on submit
        int gen = Integer.parseInt(gender);
        $("div.custom-control.custom-radio.custom-control-inline", gen).click();
        $("#userNumber").sendKeys(number);

        $("#submit").scrollIntoView(true);
        $("#submit").click();

        $("tbody > tr:nth-child(1) > td:nth-child(2)").scrollIntoView(true);
        $("tbody > tr:nth-child(1) > td:nth-child(2)").shouldBe(Condition.text(firstName + " " + lastName));

        System.out.println("#" + (tagNumber + 1) + " " + firstName + " " + lastName + " " + number);
        tagNumber++;

    }





}
