package com.amachi.app.vitalia.medicalcatalog.infrastructure.repository;

import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalUnitTypeRepository extends CommonRepository<MedicalUnitType, Long> {
    
    /**
     * ✅ GLOBAL: busca un tipo de unidad por su código único.
     */
    Optional<MedicalUnitType> findByCode(String code);

    /**
     * 🔥 GLOBAL: Verificador de existencia transversal.
     */
    boolean existsByCode(String code);
}
