package com.amachi.app.vitalia.medicalcore.doctor.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorHospitalAssignmentDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorHospitalAssignmentSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.entity.DoctorHospitalAssignment;
import com.amachi.app.vitalia.medicalcore.doctor.mapper.DoctorHospitalAssignmentMapper;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorHospitalAssignmentRepository;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.doctor.service.DoctorHospitalAssignmentService;
import com.amachi.app.vitalia.medicalcore.doctor.specification.DoctorHospitalAssignmentSpecification;
import com.amachi.app.vitalia.medicalcore.hospital.entity.Hospital;
import com.amachi.app.vitalia.medicalcore.hospital.repository.HospitalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorHospitalAssignmentServiceImpl
        extends BaseService<DoctorHospitalAssignment, DoctorHospitalAssignmentDto, DoctorHospitalAssignmentSearchDto>
        implements DoctorHospitalAssignmentService {

    private final DoctorHospitalAssignmentRepository repository;
    private final DoctorHospitalAssignmentMapper mapper;
    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<DoctorHospitalAssignment, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<DoctorHospitalAssignment> buildSpecification(DoctorHospitalAssignmentSearchDto searchDto) {
        return new DoctorHospitalAssignmentSpecification(searchDto);
    }

    @Override
    @Transactional
    public DoctorHospitalAssignment create(DoctorHospitalAssignmentDto dto) {
        if (dto == null) throw new BusinessException("DoctorHospitalAssignment cannot be null");
        Long tenantId = TenantContext.getTenantId();

        DoctorHospitalAssignment entity = mapper.toEntity(dto);

        // Validar Doctor
        if (dto.getDoctorId() == null) throw new BusinessException("Doctor is required");
        if (!doctorRepository.existsByIdAndTenantId(dto.getDoctorId(), tenantId))
            throw new BusinessException("Doctor no encontrado con ID: " + dto.getDoctorId());
        entity.setDoctor(entityManager.getReference(Doctor.class, dto.getDoctorId()));

        // Validar Hospital
        if (dto.getHospitalId() == null) throw new BusinessException("Hospital is required");
        if (!hospitalRepository.existsById(dto.getHospitalId()))
            throw new BusinessException("Hospital no encontrado con ID: " + dto.getHospitalId());
        entity.setHospital(entityManager.getReference(Hospital.class, dto.getHospitalId()));

        // Si se marca como primario, desmarcar las anteriores del mismo doctor en el mismo tenant
        if (Boolean.TRUE.equals(dto.getIsPrimary())) {
            repository.findByDoctorIdAndTenantId(dto.getDoctorId(), tenantId)
                    .forEach(prev -> { prev.setIsPrimary(false); repository.save(prev); });
        }

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(DoctorHospitalAssignmentDto dto, DoctorHospitalAssignment existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected DoctorHospitalAssignment mapToEntity(DoctorHospitalAssignmentDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(DoctorHospitalAssignment entity) { }

    @Override
    protected void publishUpdatedEvent(DoctorHospitalAssignment entity) { }
}
