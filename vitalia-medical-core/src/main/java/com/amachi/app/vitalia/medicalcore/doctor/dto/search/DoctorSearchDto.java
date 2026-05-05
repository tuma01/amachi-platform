package com.amachi.app.vitalia.medicalcore.doctor.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Search DTO for Doctors (SaaS Elite Tier).
 */
@Getter
@Setter
@NoArgsConstructor
public class DoctorSearchDto extends BaseSearchDto {
    private String licenseNumber;
    private Long departmentUnitId;
    private List<Long> specialtyIds;
    private Boolean isAvailable;
    private String nationalId;
}
