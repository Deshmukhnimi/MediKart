package com.cdac.Service;

import com.cdac.Entity.Category;
import com.cdac.Entity.Medicine;
import com.cdac.Entity.User;
import com.cdac.Exception.ResourceNotFoundException;
import com.cdac.dao.MedicineRepository;
import com.cdac.dao.UserRepository;
import com.cdac.dto.MedicineDTO;
import com.cdac.dto.MedicineRequestDTO;
import com.cdac.dto.MedicineResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public int removeExpiredMedicines() {
        List<Medicine> expiredMedicines = medicineRepository.findByExpiryDateBefore(LocalDate.now());

        int count = expiredMedicines.size();
        medicineRepository.deleteAll(expiredMedicines);

        return count;
    }

    
    //correct
    @Override
    public List<MedicineDTO> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(MedicineDTO::new) // uses the constructor in DTO
                .collect(Collectors.toList());
    }


    //correct
    @Override
    public List<MedicineResponseDTO> getExpiredMedicines() {
        LocalDate today = LocalDate.now();
        List<Medicine> allMedicines = medicineRepository.findAll();

        return allMedicines.stream()
                .filter(medicine -> medicine.getExpiryDate().isBefore(today))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ✅ You MUST add this method inside the same class
    private MedicineResponseDTO mapToResponseDTO(Medicine medicine) {
        MedicineResponseDTO dto = new MedicineResponseDTO();
        dto.setMedicineId(medicine.getMedicineId());
        dto.setName(medicine.getName());
        dto.setDescription(medicine.getDescription());
        dto.setPrice(medicine.getPrice());
        dto.setStock(medicine.getStock());
        dto.setManufacturer(medicine.getManufacturer());
        dto.setExpiryDate(medicine.getExpiryDate());
        return dto;
    }

    //correct

    @Override
    public String addMedicine(MedicineDTO medicineDTO, String userEmail) {
        // Find the user (admin) by email from JWT
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Check if the user has ADMIN role
        if (!user.getCategory().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized: Only ADMIN can add medicines");
        }

        // Create Medicine entity from DTO
        Medicine medicine = new Medicine();
        medicine.setName(medicineDTO.getName());
        medicine.setDescription(medicineDTO.getDescription());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setStock(medicineDTO.getStock());
        medicine.setManufacturer(medicineDTO.getManufacturer());
        medicine.setExpiryDate(medicineDTO.getExpiryDate());
        medicine.setAddedBy(user); // link the user

        // Save to DB
        medicineRepository.save(medicine);

        return "Medicine added successfully by " + user.getName();
    }
    
    @Override
    public List<MedicineDTO> getLowStockMedicines() {
        int LOW_STOCK_THRESHOLD = 10; // you can change as needed
        return medicineRepository.findAll()
                .stream()
                .filter(med -> med.getStock() != null && med.getStock() < LOW_STOCK_THRESHOLD)
                .map(MedicineDTO::new)
                .collect(Collectors.toList());
    }

}

