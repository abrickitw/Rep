package org.avd.kamin.repository;

import org.avd.kamin.domain.Grad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Grad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradRepository extends JpaRepository<Grad, Long>, JpaSpecificationExecutor<Grad> {}
