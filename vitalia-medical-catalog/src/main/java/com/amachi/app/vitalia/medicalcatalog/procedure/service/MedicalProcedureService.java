package com.amachi.app.vitalia.medicalcatalog.procedure.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.search.MedicalProcedureSearchDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;

public interface MedicalProcedureService extends GenericService<MedicalProcedure, MedicalProcedureDto, MedicalProcedureSearchDto> {
}
