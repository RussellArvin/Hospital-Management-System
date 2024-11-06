package model;

import java.time.LocalDateTime;

public class Administrator extends User {
    public Administrator(
        String id,
        String password,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        super(id, password, name, createdAt, updatedAt);
    }
}
