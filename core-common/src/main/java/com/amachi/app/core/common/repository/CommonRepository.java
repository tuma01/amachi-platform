package com.amachi.app.core.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Interface base para todos los repositorios de la plataforma Amachi.
 * Combina las funcionalidades de CRUD estándar y ejecución de Especificaciones (filtros dinámicos).
 *
 * @param <E> Tipo de la Entidad
 * @param <ID> Tipo del ID (usualmente Long)
 */
@NoRepositoryBean
public interface CommonRepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
}
