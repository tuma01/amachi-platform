package com.amachi.app.vitalia.medicalcore.consultation.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends TenantCommonRepository<Consultation, Long> {

    List<Consultation> findByPatientIdAndTenantId(Long patientId, Long tenantId);

    List<Consultation> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
