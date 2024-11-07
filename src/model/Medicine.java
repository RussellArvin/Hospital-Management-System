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
