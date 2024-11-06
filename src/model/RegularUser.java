package model;

import java.time.LocalDateTime;

import enums.Gender;

public abstract class RegularUser extends User {
    protected int age;
    protected Gender gender;

    public RegularUser(
        String id, 
        String password,
        String name,
        int age,
        Gender gender
    ){
        super(id,password,name);
        this.age = age;
        this.gender = gender;
    }

    public RegularUser(
        String id,
        String password,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        super(id, password, name, createdAt, updatedAt);
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toCsvString() {
        return String.join(",", 
            id, 
            password, 
            name,
            String.valueOf(age),
            gender.toString(),
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}
