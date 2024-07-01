package org.avd.kamin.repository;

import org.avd.kamin.domain.StatusEvidencije;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StatusEvidencije entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusEvidencijeRepository extends JpaRepository<StatusEvidencije, Long>, JpaSpecificationExecutor<StatusEvidencije> {}
