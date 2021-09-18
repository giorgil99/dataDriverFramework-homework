import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.testng.SoftAsserts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Listeners({SoftAsserts.class})
public class ExcelDrivenTest {


    public static Object[][] object;
    public static String value ;
    public static int tagNumber;


    public  void  excelData() throws IOException {
        File file = new File(".\\data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);

// fill First Name, Last Name , Gender and mobile number dynamically using Excel and Apache POI
      int rows = sheet.getLastRowNum();
      int columns = sheet.getRow(rows).getLastCellNum();
        object = new Object[rows][columns-1];
//        int counter =0;
        for (int i = 0; i < rows; i++) {

            Row row = sheet.getRow(i+1);

            for (int k=0; k<columns-1;k++){

            Cell cell = row.getCell(k+1);

        switch (cell.getCellType()) {

            case NUMERIC:
                ((XSSFCell) cell).setCellType(CellType.STRING);
                String doubleNumber = cell.getStringCellValue();

                 object[i][k] =doubleNumber ;
                System.out.println(object[i][k]);
              break;
            case STRING:
                  object[i][k] = cell.getStringCellValue();
                System.out.println(object[i][k]);
                  break;

        }
        }

    }

}
    @DataProvider(name = "filler1")
    public Object[][] getDataFromDataProvider() throws IOException {
        excelData();
        return object;


    }

    @Test(dataProvider = "filler1")
//     Each parameter should receive 3 different values
    public void dataTest(String firstName, String lastName, String number, String gender) {


        Configuration.startMaximized = true;
        Configuration.browser = "Edge";
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").sendKeys(firstName);
        $("#lastName").sendKeys(lastName);

//        int gen = Integer.parseInt(gender);

        if (Objects.equals(gender, "1") || Objects.equals(gender, "2"))
        {
           int trueGender = Integer.parseInt(gender);
            $("div.custom-control.custom-radio.custom-control-inline", trueGender).scrollIntoView(true);
            $("div.custom-control.custom-radio.custom-control-inline", trueGender).click();
        }

        else {
            System.out.println("Wrong gender format");
        }


        $("#userNumber").sendKeys(number);

        $("#submit").scrollIntoView(true);
        $("#submit").click();

        $("tbody > tr:nth-child(1) > td:nth-child(2)").scrollIntoView(true);

//         Validate the Student Name value dynamically

        $("tbody > tr:nth-child(1) > td:nth-child(2)").shouldBe(Condition.text(firstName + " " + lastName));

        System.out.println("#" + (tagNumber + 1) + " " + firstName + " " + lastName + " " + number);
        tagNumber++;

    }





}
