package model;

import java.time.LocalDateTime;

public class Medicine extends BaseEntity {
    private String name;
    private int stock;
    private int lowStockAlert;

    public Medicine(
        String id,
        String name,
        int stock,
        int lowStockAlert,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
    }

    public Medicine(
        String name,
        int stock,
        int lowStockAlert
    ) {
        super();
        this.name = name;
        this.stock = stock;
        this.lowStockAlert = lowStockAlert;
    }

    public String getName(){
        return this.name;
    }

    public int getStock(){
        return this.stock;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public int getLowStockAlert(){
        return this.lowStockAlert;
    }

    public void setLowStockAlert(int stock){
        this.lowStockAlert = stock;
    }

    public String toCsvString(){
        return String.join(",",
            id,
            name,
            String.valueOf(stock),
            String.valueOf(lowStockAlert),
            createdAt.toString(),
            updatedAt.toString()
        );
    }
}
