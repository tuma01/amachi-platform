package com.amachi.app.core.management.theme.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.domain.theme.dto.ThemeDto;
import com.amachi.app.core.domain.theme.dto.search.ThemeSearchDto;
import com.amachi.app.core.domain.theme.entity.Theme;
import com.amachi.app.core.management.theme.mapper.ThemeMapper;
import com.amachi.app.core.management.theme.service.ThemeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeController extends BaseController implements ThemeApi {

    private final ThemeService themeService;
    private final ThemeMapper mapper;

    // Standard CRUD Implementation
    @Override
    public ResponseEntity<ThemeDto> getThemeById(Long id) {
        Theme entity = themeService.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<ThemeDto> createTheme(ThemeDto dto) {
        Theme savedEntity = themeService.create(dto);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ThemeDto> updateTheme(Long id, ThemeDto dto) {
        Theme updatedEntity = themeService.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteTheme(Long id) {
        themeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        List<Theme> entities = themeService.getAll();
        return ResponseEntity.ok(entities.stream().map(mapper::toDto).toList());
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponseDto<ThemeDto>> getPaginatedThemes(ThemeSearchDto searchDto,
            Integer pageIndex, Integer pageSize) {
        Page<Theme> page = themeService.getAll(searchDto, pageIndex, pageSize);
        List<ThemeDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<ThemeDto> response = PageResponseDto.<ThemeDto>builder()
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

    // =====================================================================
    // Specialized Branding Endpoints (Legacy/Mobile support)
    // =====================================================================

    // Public: returns theme by tenantCode
    @GetMapping(value = "/tenant/{tenantCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeDto> getThemeByTenant(@PathVariable String tenantCode) {
        ThemeDto dto = themeService.getThemeForTenant();
        return ResponseEntity.ok(dto);
    }

    // Update by tenantCode: only tenant admin or super-admin
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PutMapping(value = "/tenant/{tenantCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeDto> upgradeTheme(@PathVariable String tenantCode,
            @Valid @RequestBody ThemeDto dto) {
        return ResponseEntity.ok(themeService.upgradeTheme(dto));
    }

    // Upload logo (multipart): only ADMIN or SUPER_ADMIN
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    @PostMapping(value = "/tenant/{tenantCode}/logo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThemeDto> uploadLogo(@PathVariable String tenantCode,
            @RequestParam("file") MultipartFile file) {
        ThemeDto dto = themeService.uploadLogo(file);
        return ResponseEntity.ok(dto);
    }
}
