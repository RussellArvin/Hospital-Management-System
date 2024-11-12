package util;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Utility class for reading Microsoft Excel (.xlsx) files.
 * This implementation uses Java's built-in libraries to read Excel files
 * by treating them as ZIP archives containing XML files.
 * 
 * <p>Features:
 * <ul>
 *   <li>Reads .xlsx files without external dependencies</li>
 *   <li>Extracts data from the first worksheet</li>
 *   <li>Skips header row automatically</li>
 *   <li>Handles shared strings and different cell types</li>
 * </ul>
 * 
 * <p>Example usage:
 * <pre>
 * List<String> rows = ExcelReader.readExcel("data.xlsx");
 * for (String row : rows) {
 *     String[] columns = row.split(",");
 *     // Process the data...
 * }
 * </pre>
 *
 * @author Russell Arvin
 * @version 1.0
 */
public class ExcelReader {

    /**
     * Reads an Excel file and returns its contents as a list of strings.
     * Each string represents one row with values separated by commas.
     * The header row (first row) is automatically skipped.
     *
     * @param fileName path to the Excel file (.xlsx format)
     * @return List of strings, each representing a row of data (comma-separated values)
     * @throws IOException if there are issues reading the file
     * @throws IllegalArgumentException if the file doesn't exist or isn't a valid .xlsx file
     */
    public static List<String> readExcel(String fileName) throws IOException {
        // Validate file
        validateFile(fileName);
        
        List<String> rows = new ArrayList<>();
        boolean isFirstRow = true;

        try (ZipFile zipFile = new ZipFile(fileName)) {
            // Get sheet data
            ZipEntry sheet = getFirstSheet(zipFile);
            if (sheet == null) {
                throw new IOException("No worksheet found in the Excel file");
            }

            // Read shared strings
            List<String> sharedStrings = readSharedStrings(zipFile);

            // Parse sheet data
            Document doc = parseSheet(zipFile, sheet);
            NodeList rowNodes = doc.getElementsByTagName("row");

            // Process each row
            for (int i = 0; i < rowNodes.getLength(); i++) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;  // Skip header row
                }

                String rowData = processRow((Element) rowNodes.item(i), sharedStrings);
                if (!rowData.isEmpty()) {
                    rows.add(rowData);
                }
            }
        } catch (Exception e) {
            throw new IOException("Error reading Excel file: " + e.getMessage(), e);
        }

        return rows;
    }

    /**
     * Validates that the input file exists and is an .xlsx file.
     *
     * @param fileName path to the file to validate
     * @throws IllegalArgumentException if validation fails
     */
    private static void validateFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + fileName);
        }
        if (!fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("Not an Excel file (.xlsx): " + fileName);
        }
    }

    /**
     * Finds and returns the first worksheet in the Excel file.
     *
     * @param zipFile the Excel file opened as a ZipFile
     * @return ZipEntry for the first worksheet, or null if none found
     */
    private static ZipEntry getFirstSheet(ZipFile zipFile) {
        return zipFile.stream()
            .filter(entry -> entry.getName().startsWith("xl/worksheets/sheet"))
            .findFirst()
            .orElse(null);
    }

    /**
     * Reads the shared strings table from the Excel file.
     * Excel uses this table to store repeated text values efficiently.
     *
     * @param zipFile the Excel file opened as a ZipFile
     * @return List of shared strings in order of their indices
     * @throws Exception if there are issues reading or parsing the shared strings
     */
    private static List<String> readSharedStrings(ZipFile zipFile) throws Exception {
        List<String> sharedStrings = new ArrayList<>();
        ZipEntry stringsEntry = zipFile.getEntry("xl/sharedStrings.xml");
        
        if (stringsEntry != null) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(zipFile.getInputStream(stringsEntry));
            NodeList strings = doc.getElementsByTagName("t");
            
            for (int i = 0; i < strings.getLength(); i++) {
                sharedStrings.add(strings.item(i).getTextContent());
            }
        }
        
        return sharedStrings;
    }

    /**
     * Parses an Excel worksheet XML file into a Document object.
     *
     * @param zipFile the Excel file opened as a ZipFile
     * @param sheet the worksheet entry to parse
     * @return Document object representing the worksheet
     * @throws Exception if there are issues parsing the worksheet
     */
    private static Document parseSheet(ZipFile zipFile, ZipEntry sheet) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(zipFile.getInputStream(sheet));
    }

    /**
     * Processes a row from the Excel sheet and converts it to a comma-separated string.
     *
     * @param row the row element from the XML document
     * @param sharedStrings list of shared strings from the workbook
     * @return comma-separated string of cell values
     */
    private static String processRow(Element row, List<String> sharedStrings) {
        NodeList cells = row.getElementsByTagName("c");
        StringBuilder rowContent = new StringBuilder();

        for (int i = 0; i < cells.getLength(); i++) {
            if (i > 0) rowContent.append(",");
            String cellValue = getCellValue((Element) cells.item(i), sharedStrings);
            rowContent.append(escapeCsvValue(cellValue));
        }

        return rowContent.toString();
    }

    /**
     * Extracts the value from an Excel cell, handling different cell types.
     *
     * @param cell the cell element from the XML document
     * @param sharedStrings list of shared strings from the workbook
     * @return the cell's value as a string
     */
    private static String getCellValue(Element cell, List<String> sharedStrings) {
        NodeList values = cell.getElementsByTagName("v");
        if (values.getLength() == 0) return "";

        String value = values.item(0).getTextContent();
        String type = cell.getAttribute("t");

        if ("s".equals(type)) {
            // Shared string type
            try {
                int index = Integer.parseInt(value);
                return sharedStrings.get(index);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return "";
            }
        }

        return value;
    }

    /**
     * Escapes special characters in a value for CSV format.
     * Wraps values containing commas in quotes and escapes existing quotes.
     *
     * @param value the string to escape
     * @return the escaped string
     */
    private static String escapeCsvValue(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Demonstrates usage of the ExcelReader class.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            String fileName = "example.xlsx";
            System.out.println("Reading Excel file: " + fileName);
            
            List<String> rows = readExcel(fileName);
            System.out.println("\nData from Excel file:");
            for (String row : rows) {
                System.out.println(row);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}