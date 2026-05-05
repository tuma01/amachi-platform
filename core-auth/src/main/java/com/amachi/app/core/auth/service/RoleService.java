package com.amachi.app.core.auth.service;

import com.amachi.app.core.auth.dto.RoleDto;
import com.amachi.app.core.auth.dto.search.RoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.common.service.GenericService;

import java.util.List;

public interface RoleService extends GenericService<Role, RoleDto, RoleSearchDto> {
    List<Role> findByNames(List<String> names);
    Role findByName(String name);
}
