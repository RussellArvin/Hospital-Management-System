package repository.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.RandomAccessFile;
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
   try {
       // Read last character to check if file ends with newline
       boolean needsNewline = false;
       File file = new File(filePath);
       
       if (file.length() > 0) {
           try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
               if (file.length() > 0) {
                   raf.seek(file.length() - 1);
                   int lastChar = raf.read();
                   needsNewline = lastChar != '\n';
               }
           }
       }

       // Open file in append mode
       try (FileWriter fw = new FileWriter(filePath, true)) {
           // If file is empty, write header
           if (file.length() == 0) {
               fw.write(header + System.lineSeparator());
           }
           // Add newline if needed
           else if (needsNewline) {
               fw.write(System.lineSeparator());
           }
           
           // Write the new line with a line separator
           fw.write(line + System.lineSeparator());
       }
   } catch (IOException e) {
       throw new RuntimeException("Error appending to CSV: " + e.getMessage());
   }
}

    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        boolean isFirstLine = true;  // Flag to track header line
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;  // Skip the header line
                }
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

    public List<String> findLinesByColumnValue(String columnName, String searchValue) {
        if (columnName == null || columnName.trim().isEmpty()) {
            throw new IllegalArgumentException("Column name cannot be null or empty");
        }
        if (searchValue == null) {
            throw new IllegalArgumentException("Search value cannot be null");
        }
    
        List<String> matchingLines = new ArrayList<>();
        boolean headerProcessed = false;
        int columnIndex = -1;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process header on first line
                if (!headerProcessed) {
                    String[] headers = line.split(",");
                    for (int i = 0; i < headers.length; i++) {
                        if (headers[i].trim().equals(columnName)) {
                            columnIndex = i;
                            break;
                        }
                    }
                    
                    if (columnIndex == -1) {
                        throw new IllegalArgumentException("Column name '" + columnName + "' not found in CSV headers");
                    }
                    
                    headerProcessed = true;
                    continue;
                }
    
                // Process data lines
                try {
                    String[] values = line.split(",");
                    if (values.length > columnIndex && values[columnIndex].trim().equals(searchValue)) {
                        matchingLines.add(line);
                    }
                } catch (Exception e) {
                    System.err.println("Warning: Failed to process line: " + line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
        
        return matchingLines;
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

    public void deleteLine(String id) {
        List<String> allLines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Add header first
            allLines.add(reader.readLine());
            
            // Read remaining lines
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(id + ",")) {
                    found = true;
                    continue; // Skip this line (delete)
                }
                allLines.add(line);
            }
            
            if (!found) {
                throw new RuntimeException("No record found with ID: " + id);
            }
            
            // Write back all lines except the deleted one
            writeAllLines(allLines);
            
        } catch (IOException e) {
            throw new RuntimeException("Error deleting from CSV: " + e.getMessage());
        }
    }

}