package com.amachi.app.core.management.theme.repository;

import com.amachi.app.core.domain.theme.entity.Theme;
import com.amachi.app.core.common.repository.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends CommonRepository<Theme, Long> {

    Optional<Theme> findByCode(String defaultTheme);

    java.util.List<Theme> findAllByTemplateTrue();
}
