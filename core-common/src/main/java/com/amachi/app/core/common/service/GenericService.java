package com.amachi.app.core.common.service;

import com.amachi.app.core.common.dto.BaseSearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface Core para servicios de la aplicación.
 * PUBLIC para ser visible desde todos los módulos (core-geography, etc).
 */
/**
 * Interface Core para servicios de la aplicación (SaaS Elite Tier).
 * @param <E> Entidad de Dominio
 * @param <D> DTO / Command de entrada para creación y actualización
 * @param <F> DTO de Filtros / Búsqueda
 */
public interface GenericService<E, D, F extends BaseSearchDto> {

    List<E> getAll();

    Page<E> getAll(F searchDto, Integer pageIndex, Integer pageSize);

    E getById(Long id);

    E create(D dto);

    E update(Long id, D dto);

    void delete(Long id);
}
