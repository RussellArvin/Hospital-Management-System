package model;

public interface Entity {
    String getId();
    void setId(String id);
    String toCsvString();
}