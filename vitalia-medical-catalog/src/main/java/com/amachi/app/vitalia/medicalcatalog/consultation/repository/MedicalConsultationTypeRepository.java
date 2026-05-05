package com.amachi.app.vitalia.medicalcatalog.consultation.repository;

import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalConsultationTypeRepository extends CommonRepository<MedicalConsultationType, Long> {
    
    /**
     * ✅ GLOBAL: busca un tipo de consulta por su código único.
     */
    Optional<MedicalConsultationType> findByCode(String code);

    /**
     * 🔥 GLOBAL: Verificador de existencia transversal.
     */
    boolean existsByCode(String code);
}
