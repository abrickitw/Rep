package org.avd.kamin.repository;

import org.avd.kamin.domain.VrstaUsluge;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VrstaUsluge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VrstaUslugeRepository extends JpaRepository<VrstaUsluge, Long>, JpaSpecificationExecutor<VrstaUsluge> {}
