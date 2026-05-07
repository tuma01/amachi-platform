package com.amachi.app.vitalia.medicalcore.episodeofcare.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.common.enums.EpisodeOfCareStatus;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeOfCareRepository extends TenantCommonRepository<EpisodeOfCare, Long> {

    List<EpisodeOfCare> findByPatientIdAndTenantId(Long patientId, Long tenantId);

    List<EpisodeOfCare> findByPatientIdAndStatusAndTenantId(Long patientId, EpisodeOfCareStatus status, Long tenantId);
}
