package com.amachi.app.vitalia.medicalcore.employee.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.employee.dto.EmployeeDto;
import com.amachi.app.vitalia.medicalcore.employee.dto.search.EmployeeSearchDto;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import com.amachi.app.vitalia.medicalcore.employee.mapper.EmployeeMapper;
import com.amachi.app.vitalia.medicalcore.employee.service.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController extends BaseController implements EmployeeApi {

    private final EmployeeService service;
    private final EmployeeMapper mapper;

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody @NonNull EmployeeDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<EmployeeDto> updateEmployee(@NonNull Long id, @Valid @RequestBody @NonNull EmployeeDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteEmployee(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<EmployeeDto>> getPaginatedEmployees(@NonNull EmployeeSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Employee> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<EmployeeDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
