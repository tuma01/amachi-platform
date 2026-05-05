package com.amachi.app.core.domain.repository;

import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.domain.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends CommonRepository<Person, Long> {

    Optional<Person> findByNationalId(String nationalId); // nationalId = CI / Passport
    Optional<Person> findByNationalHealthId(String nationalHealthId);
    Optional<Person> findByEmail(String email);

}
