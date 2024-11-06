package ui;

public abstract class MenuUI { 
   protected abstract void printOptions();
   
   protected void printMenu(String title, String... options) {
       int maxLength = title.length();
       for (String option : options) {
           maxLength = Math.max(maxLength, option.length());
       }
       
       maxLength += 6;  
       String border = "=".repeat(maxLength);
       
       System.out.println("\n" + border);
       System.out.println("   " + title + " ".repeat(maxLength - title.length() - 3));
       System.out.println(border);
       
       for (String option : options) {
           System.out.println(option + " ".repeat(maxLength - option.length()));
       }
       
       System.out.println(border);
   }
}