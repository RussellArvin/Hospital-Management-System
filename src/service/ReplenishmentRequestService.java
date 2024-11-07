package service;

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

    public ReplenishmentRequestDetail[] getRequests() {
        ReplenishmentRequest[] requests = replenishmentRequestRepository.findAll();
        ReplenishmentRequestDetail[] details = new ReplenishmentRequestDetail[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            ReplenishmentRequest request = requests[i];
            
            // Find associated Medicine and Pharmacist
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
}
