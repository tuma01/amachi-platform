package com.amachi.app.core.domain.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.domain.dto.PersonDto;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper central para la Identidad Global (Person).
 * Simplificado para asegurar la compilación (Modo Supervivencia).
 */
@Mapper(config = BaseMapperConfig.class, uses = {AddressMapper.class}, builder = @Builder(disableBuilder = true))
public interface PersonMapper {

    @AuditableIgnoreConfig.IgnorePureAuditableFields
//    @Mapping(target = "isDeleted", ignore = true)
    Person toEntity(PersonDto dto);

    PersonDto toDto(Person entity);

    void updateEntityFromDto(PersonDto dto, @MappingTarget Person entity);
}
