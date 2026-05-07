package com.amachi.app.vitalia.medicalcore.doctor.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.doctor.entity.DoctorHospitalAssignment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorHospitalAssignmentRepository extends TenantCommonRepository<DoctorHospitalAssignment, Long> {

    List<DoctorHospitalAssignment> findByDoctorIdAndTenantId(Long doctorId, Long tenantId);

    List<DoctorHospitalAssignment> findByHospitalIdAndTenantId(Long hospitalId, Long tenantId);

    Optional<DoctorHospitalAssignment> findByDoctorIdAndHospitalIdAndTenantId(Long doctorId, Long hospitalId, Long tenantId);

    boolean existsByDoctorIdAndHospitalIdAndTenantId(Long doctorId, Long hospitalId, Long tenantId);
}
