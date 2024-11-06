package repository.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CsvFileManager {
    private final String filePath;
    private final String header;

    public CsvFileManager(String filePath, String header) {
        this.filePath = filePath;
        this.header = header;
        createFileIfNotExists();
    }

    public String getHeader(){
        return this.header;
    }

    public void createFileIfNotExists() {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(header);
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }

    public String readLine(String searchId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                if (line.startsWith(searchId + ",")) {
                    return line;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return null;
    }

    public void appendLine(String line) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            // If file is empty, write header first
            if (new File(filePath).length() == 0) {
                out.println(header);
            }
            
            out.println(line);
            
        } catch (IOException e) {
            throw new RuntimeException("Error appending to CSV: " + e.getMessage());
        }
    }

    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV: " + e.getMessage());
        }
        return lines;
    }

    public void writeAllLines(List<String> lines) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to CSV: " + e.getMessage());
        }
    }


    public void writeLine(String line) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            
            out.println(line);
            
        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }

    public String findLineByColumnValue(String columnName, String searchValue) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read header line first
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return null;
            }
    
            // Split header into columns and find the index of the target column
            String[] headers = headerLine.split(",");
            int columnIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equals(columnName)) {
                    columnIndex = i;
                    break;
                }
            }
    
            // If column name not found in headers
            if (columnIndex == -1) {
                throw new IllegalArgumentException("Column name '" + columnName + "' not found in CSV headers");
            }
    
            // Read through file looking for matching value in the correct column
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // Check if the line has enough columns and the value matches
                if (values.length > columnIndex && values[columnIndex].trim().equals(searchValue)) {
                    return line;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error searching CSV: " + e.getMessage());
        }
        return null;
    }

}