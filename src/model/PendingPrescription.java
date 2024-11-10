package model;

public class PendingPrescription {
    private String medicineId;
    private int amount;

    public PendingPrescription(
        String medicineId,
        int amount
    ){
        this.medicineId = medicineId;
        this.amount = amount;
    }

    public String getMedicineId(){
        return this.medicineId;
    }

    public int getAmount(){
        return this.amount;
    }
}
