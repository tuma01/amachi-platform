package com.amachi.app.core.domain.hospital.repository;

import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.domain.hospital.entity.Hospital;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends CommonRepository<Hospital, Long> {

    /**
     * ✅ Valida existencia de Tax ID (NIT/RUT) para evitar duplicidad legal de hospitales.
     */
    boolean existsByTaxId(String taxId);
}
