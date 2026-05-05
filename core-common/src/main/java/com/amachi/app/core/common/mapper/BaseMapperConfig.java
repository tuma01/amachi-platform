package com.amachi.app.core.common.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * Global Configuration for MapStruct (SaaS Elite Tier).
 * Enforces Constructor Injection and strict unmapped policies.
 */
@MapperConfig(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, injectionStrategy = InjectionStrategy.CONSTRUCTOR, collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface BaseMapperConfig {
}
