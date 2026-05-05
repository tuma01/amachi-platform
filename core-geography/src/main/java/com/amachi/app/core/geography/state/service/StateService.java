package com.amachi.app.core.geography.state.service;

import com.amachi.app.core.geography.state.dto.StateDto;
import com.amachi.app.core.geography.state.dto.search.StateSearchDto;
import com.amachi.app.core.geography.state.entity.State;
import com.amachi.app.core.common.service.GenericService;

/**
 * Contrato de servicio para Estados/Departamentos (SaaS Elite Tier).
 */
public interface StateService extends GenericService<State, StateDto, StateSearchDto> {
}
