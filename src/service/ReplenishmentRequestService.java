package service;

import enums.ReplenishmentRequestStatus;
import model.Medicine;
import model.Pharmacist;
import model.ReplenishmentRequest;
import model.ReplenishmentRequestDetail;
import repository.MedicineRepository;
import repository.PharmacistRepository;
import repository.ReplenishmentRequestRepository;

/**
 * The ReplenishmentRequestService class provides functionality for managing 
 * replenishment requests for medicines, including creation, status updates, 
 * and retrieval of requests.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class ReplenishmentRequestService {
    private ReplenishmentRequestRepository replenishmentRequestRepository;
    private MedicineRepository medicineRepository;
    private PharmacistRepository pharmacistRepository;

    /**
     * Constructs a ReplenishmentRequestService with the required repositories.
     *
     * @param replenishmentRequestRepository Repository for managing replenishment requests.
     * @param medicineRepository             Repository for managing medicine data.
     * @param pharmacistRepository           Repository for managing pharmacist data.
     */
    public ReplenishmentRequestService(
        ReplenishmentRequestRepository replenishmentRequestRepository,
        MedicineRepository medicineRepository,
        PharmacistRepository pharmacistRepository
    ) {
        this.replenishmentRequestRepository = replenishmentRequestRepository;
        this.medicineRepository = medicineRepository;
        this.pharmacistRepository = pharmacistRepository;
    }

    /**
     * Retrieves the ID of a medicine based on its name.
     *
     * @param medicineName The name of the medicine.
     * @return The ID of the medicine, or null if not found.
     */
    public String getMedicineId(String medicineName) {
        try {
            Medicine medicine = this.medicineRepository.findOneByName(medicineName);
            if (medicine == null) return null;
            return medicine.getId();
        } catch (Exception e) {
            System.out.println("Something went wrong when checking existence of medicine!");
            return null;
        }
    }

    /**
     * Creates a new replenishment request for a medicine.
     *
     * @param medicineId    The ID of the medicine.
     * @param pharmacistId  The ID of the pharmacist making the request.
     * @param newAmount     The amount to be replenished.
     * @return null if successful, or an error message if creation fails.
     */
    public String createRequest(
        String medicineId,
        String pharmacistId,
        int newAmount
    ) {
        try {
            Medicine medicine = this.medicineRepository.findOne(medicineId);
            Pharmacist pharmacist = this.pharmacistRepository.findOne(pharmacistId);
            if (medicine == null) return "Unable to identify medicine";
            if (pharmacist == null) return "Unable to identify pharmacist";

            ReplenishmentRequest request = new ReplenishmentRequest(medicineId, pharmacistId, newAmount);
            this.replenishmentRequestRepository.save(request);
            return null;
        } catch (Exception e) {
            return "Something went wrong when creating request";
        }
    }

    /**
     * Updates the status of a replenishment request.
     *
     * @param requestId The ID of the replenishment request.
     * @param status    The new status to set for the request.
     * @return null if successful, or an error message if the update fails.
     */
    public String updateRequestStatus(
        String requestId,
        ReplenishmentRequestStatus status
    ) {
        try {
            ReplenishmentRequest request = this.replenishmentRequestRepository.findOne(requestId);
            if (request == null) return "Request can't be found";

            if (status == ReplenishmentRequestStatus.APPROVED) {
                Medicine medicine = this.medicineRepository.findOne(request.getMedicineId());
                medicine.setStock(medicine.getStock() + request.getNewAmount());

                medicineRepository.update(medicine);
            }

            request.setStatus(status);
            this.replenishmentRequestRepository.update(request);
            return null;
        } catch (Exception e) {
            return "Something went wrong with updating request Status";
        }
    }

    /**
     * Retrieves all pending replenishment requests with their details.
     *
     * @return An array of ReplenishmentRequestDetail objects.
     */
    public ReplenishmentRequestDetail[] getRequests() {
        ReplenishmentRequest[] requests = replenishmentRequestRepository.findManyByStatus(ReplenishmentRequestStatus.PENDING);
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

    /**
     * Retrieves all replenishment requests made by a specific pharmacist with their details.
     *
     * @param pharmacistID The ID of the pharmacist.
     * @return An array of ReplenishmentRequestDetail objects for the pharmacist.
     */
    public ReplenishmentRequestDetail[] getPharmacistRequests(String pharmacistID) {
        Pharmacist pharmacist = pharmacistRepository.findOne(pharmacistID);
        ReplenishmentRequest[] requests = replenishmentRequestRepository.findManyByPharmacist(pharmacistID);
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
