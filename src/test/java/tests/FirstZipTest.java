package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

public class FirstZipTest {

    @Test
    void zipShouldContainsFilesTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files.zip"));

        long fileCount = zf.stream().count();
        System.out.println("Файлов в архиве: " + fileCount);
        assertTrue(fileCount > 0, "Архив должен содержать файлы");

        System.out.println("Список файлов:");
        zf.stream().forEach(entry -> {
            System.out.println("  - " + entry.getName() + " (" + entry.getSize() + " байт)");
        });

        zf.close();
    }

    @Test
    void pdfTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files.zip"));
        ZipEntry pdfEntry = zf.stream()
                .filter(e -> e.getName().endsWith(".pdf"))
                .findFirst()
                .orElseThrow(() -> new Exception("PDF файл не найден в архиве"));
        try (InputStream is = zf.getInputStream(pdfEntry)) {
            PDF pdf = new PDF(is);
            assertTrue(pdf.text.contains("Привет") || pdf.text.contains("Мир"),
                    "PDF должен содержать слово 'Привет' или 'Мир'");
            assertTrue(pdf.numberOfPages > 0, "PDF должен содержать хотя бы одну страницу");

        }

    }

    @Test
    void csvTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files.zip"));
        ZipEntry csvEntry = zf.stream()
                .filter(e -> e.getName().endsWith(".csv"))
                .findFirst()
                .orElseThrow(() -> new Exception("CSV файл не найден в архиве"));
        try (InputStream is = zf.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(is, "UTF-8"));
            List<String[]> rows = reader.readAll();
            assertThat(rows.get(0)).contains(
                    "ID",
                    "Name",
                    "Age",
                    "Country"
            );

        }

    }

    @Test
    void xlsxTest() throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/files.zip"));
        ZipEntry xlsxEntry = zf.stream()
                .filter(e -> e.getName().endsWith(".xlsx"))
                .findFirst()
                .orElseThrow(() -> new Exception("xlsx файл не найден в архиве"));
        try (InputStream is = zf.getInputStream(xlsxEntry)) {
            XLS xls = new XLS(is);

            String actualValue = xls.excel.getSheetAt(0).getSheetName();
            String value = xls.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue();

            Assertions.assertTrue(value.contains("Бучельников"));
            Assertions.assertTrue(actualValue.contains("Template"));

        }

    }

}
