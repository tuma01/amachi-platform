package com.amachi.app.core.geography.country.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.core.geography.country.dto.search.CountrySearchDto;
import com.amachi.app.core.geography.country.entity.Country;
import com.amachi.app.core.geography.country.mapper.CountryMapper;
import com.amachi.app.core.geography.country.service.impl.CountryServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryController extends BaseController implements CountryApi {

    private final CountryServiceImpl service;
    private final CountryMapper mapper;

    @Override
    public ResponseEntity<CountryDto> getCountryById(@NonNull Long id) {
        Country entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<CountryDto> createCountry(@Valid @RequestBody @NonNull CountryDto dto) {
        Country savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    public ResponseEntity<CountryDto> updateCountry(@NonNull Long id, @Valid @RequestBody @NonNull CountryDto dto) {
        Country updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteCountry(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        List<Country> entities = service.getAll();
        List<CountryDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<CountryDto>> getPaginatedCountries(
            @NonNull CountrySearchDto countrySearchDto, Integer pageIndex, Integer pageSize) {
        Page<Country> page = service.getAll(countrySearchDto, pageIndex, pageSize);

        return ResponseEntity.ok(PageResponseDto.<CountryDto>builder()
                .content(mapper.toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build());
    }
}
