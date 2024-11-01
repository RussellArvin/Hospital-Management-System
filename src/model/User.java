package model;
import model.BaseEntity;
import java.util.UUID;

public abstract class User extends BaseEntity{
    protected String password;
    protected String name;

    public User(String id, String name){
        super(id);
        this.password = "password";
        this.name = name;
    }

    public User(String id, String password, String name){
        super(id);
        this.password = password;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }
    
    @Override
    public abstract String toCsvString();

    public abstract void displayMenu();
}