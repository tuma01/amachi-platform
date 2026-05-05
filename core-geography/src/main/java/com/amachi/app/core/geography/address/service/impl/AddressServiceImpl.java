package com.amachi.app.core.geography.address.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.dto.search.AddressSearchDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.event.AddressCreatedEvent;
import com.amachi.app.core.geography.address.event.AddressUpdatedEvent;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.repository.AddressRepository;
import com.amachi.app.core.geography.address.service.AddressService;
import com.amachi.app.core.geography.address.specification.AddressSpecification;
import com.amachi.app.core.geography.country.entity.Country;
import com.amachi.app.core.geography.municipality.entity.Municipality;
import com.amachi.app.core.geography.province.entity.Province;
import com.amachi.app.core.geography.state.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor de Gestión de Direcciones (SaaS Elite Tier).
 * Elevado al estándar DTO-First de 3 genéricos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl extends BaseService<Address, AddressDto, AddressSearchDto> implements AddressService {

    private final AddressRepository addressRepository;
    private final DomainEventPublisher eventPublisher;
    private final AddressMapper mapper;

    @PersistenceContext
    private EntityManager em;

    @Override
    protected CommonRepository<Address, Long> getRepository() {
        return addressRepository;
    }

    @Override
    protected Specification<Address> buildSpecification(AddressSearchDto searchDto) {
        return new AddressSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Address mapToEntity(AddressDto dto) {
        Address address = mapper.toEntity(dto);
        resolveGeographicRefs(dto, address);
        return address;
    }

    @Override
    protected void mergeEntities(AddressDto dto, Address existing) {
        mapper.updateEntityFromDto(dto, existing);
        resolveGeographicRefs(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Address entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new AddressCreatedEvent(entity.getId(), entity.getCity()));
        }
    }

    @Override
    protected void publishUpdatedEvent(Address entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new AddressUpdatedEvent(entity.getId(), entity.getCity()));
        }
    }

    private void resolveGeographicRefs(AddressDto dto, Address address) {
        address.setCountry(dto.getCountryId() != null ? em.getReference(Country.class, dto.getCountryId()) : null);
        address.setState(dto.getStateId() != null ? em.getReference(State.class, dto.getStateId()) : null);
        address.setProvince(dto.getProvinceId() != null ? em.getReference(Province.class, dto.getProvinceId()) : null);
        address.setMunicipality(dto.getMunicipalityId() != null ? em.getReference(Municipality.class, dto.getMunicipalityId()) : null);
    }
}
