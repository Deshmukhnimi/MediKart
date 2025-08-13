package com.cdac.Service;

import com.cdac.Entity.Medicine;
import com.cdac.dto.MedicineDTO;
import com.cdac.dto.MedicineRequestDTO;
import com.cdac.dto.MedicineResponseDTO;

import java.util.List;

public interface MedicineService {
 
   //done
	String addMedicine(MedicineDTO medicineDTO, String userEmail);
	List<MedicineDTO> getAllMedicines();
    List<MedicineResponseDTO> getExpiredMedicines();
    List<MedicineDTO> getLowStockMedicines();
    int removeExpiredMedicines();

}