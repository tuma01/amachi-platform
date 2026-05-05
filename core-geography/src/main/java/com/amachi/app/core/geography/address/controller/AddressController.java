package com.amachi.app.core.geography.address.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.dto.search.AddressSearchDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.impl.AddressServiceImpl;
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
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController extends BaseController implements AddressApi {

    private final AddressServiceImpl service;
    private final AddressMapper mapper;

    @Override
    public ResponseEntity<AddressDto> getAddressById(@NonNull @PathVariable Long id) {
        Address entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody @NonNull AddressDto dto) {
        Address savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    public ResponseEntity<AddressDto> updateAddress(@NonNull Long id, @Valid @RequestBody @NonNull AddressDto dto) {
        Address updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteAddress(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<Address> entities = service.getAll();
        List<AddressDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<AddressDto>> getPaginatedAddresses(
            @NonNull AddressSearchDto searchDto,
            Integer pageIndex, Integer pageSize) {
        Page<Address> page = service.getAll(searchDto, pageIndex,
                pageSize);
        List<AddressDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        PageResponseDto<AddressDto> response = PageResponseDto.<AddressDto>builder()
                .content(dtos)
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }
}
