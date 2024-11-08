package service;

import enums.ReplenishmentRequestStatus;
import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequest;
import model.ReplenishmentRequestDetail;
import repository.MedicineRepository;
import repository.PharmacistRepository;
import repository.ReplenishmentRequestRepository;

public class ReplenishmentRequestService {
    private ReplenishmentRequestRepository replenishmentRequestRepository;
    private MedicineRepository medicineRepository;
    private PharmacistRepository pharmacistRepository;

    public ReplenishmentRequestService(
        ReplenishmentRequestRepository replenishmentRequestRepository,
        MedicineRepository medicineRepository,
        PharmacistRepository pharmacistRepository
    ){
        this.replenishmentRequestRepository = replenishmentRequestRepository;
        this.medicineRepository = medicineRepository;
        this.pharmacistRepository = pharmacistRepository;
    }

    public String getMedicineId(String medicineName){
        try{
            Medicine medicine = this.medicineRepository.findOneByName(medicineName);
            if(medicine == null) return null;
            return medicine.getId();
        } catch(Exception e){
            System.out.println("Something went wrong when checking existence of medicine!");
            return null;
        }
    }

    public String createRequest(
        String medicineId,
        String pharmacistId,
        int newAmount
    ){
        try{
            Medicine medicine = this.medicineRepository.findOne(medicineId);
            Pharmacist pharmacist = this.pharmacistRepository.findOne(pharmacistId);
            if(medicine == null) return "Unable to identify medicine";
            if(pharmacist == null) return "Unable to identify pharmacist";

            ReplenishmentRequest request = new ReplenishmentRequest(medicineId, pharmacistId, newAmount);
            this.replenishmentRequestRepository.save(request);
            return null;
        }catch(Exception e){
            return "Something went wrong when creating request";
        }
    }

    public String updateRequestStatus(
        String requestId,
        ReplenishmentRequestStatus status
    ) {
        try{
            ReplenishmentRequest request = this.replenishmentRequestRepository.findOne(requestId);
            if(request == null) return "Request can't be found";

            if(status == ReplenishmentRequestStatus.APPROVED){
                Medicine medicine = this.medicineRepository.findOne(request.getMedicineId());
                medicine.setStock(medicine.getStock() + request.getNewAmount());

                medicineRepository.update(medicine);
            }

            request.setStatus(status);
            this.replenishmentRequestRepository.update(request);
            return null;
        }catch(Exception e){
            return "Something went wrong with updating request Status";
        }
    }

    public ReplenishmentRequestDetail[] getRequests() {
        ReplenishmentRequest[] requests = replenishmentRequestRepository.findByStatus(ReplenishmentRequestStatus.PENDING);
        ReplenishmentRequestDetail[] details = new ReplenishmentRequestDetail[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            ReplenishmentRequest request = requests[i];
            
            Medicine medicine = medicineRepository.findOne(request.getMedicineId());
            Pharmacist pharmacist = pharmacistRepository.findOne(request.getPharmacistId());
            
            // Create detail object using the static factory method
            details[i] = ReplenishmentRequestDetail.fromReplenishmentRequest(
                request,
                medicine,
                pharmacist
            );
        }
        
        return details;
    }

    public ReplenishmentRequestDetail[] getPharmacistRequests(String pharmacistID){
        Pharmacist pharmacist = pharmacistRepository.findOne(pharmacistID);
        ReplenishmentRequest[] requests = replenishmentRequestRepository.findByPharmacist(pharmacistID);
        ReplenishmentRequestDetail[] details = new ReplenishmentRequestDetail[requests.length];

        for (int i = 0; i < requests.length; i++) {
            ReplenishmentRequest request = requests[i];
            Medicine medicine = medicineRepository.findOne(request.getMedicineId());
            
            // Create detail object using the static factory method
            details[i] = ReplenishmentRequestDetail.fromReplenishmentRequest(
                request,
                medicine,
                pharmacist
            );
        }
        
        return details;

    }
}
