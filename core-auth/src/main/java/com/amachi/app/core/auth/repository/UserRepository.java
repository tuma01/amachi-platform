package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.domain.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {

    /**
     * Busca un usuario por su email.
     *
     * @param email el email del usuario
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el email proporcionado.
     *
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);

    Optional<User> findByPerson(Person person);

    // Optional<User> findByResetToken(String resetToken);
}
