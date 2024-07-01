package org.avd.kamin.repository;

import java.util.List;
import java.util.Optional;
import org.avd.kamin.domain.Evidencija;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Evidencija entity.
 */
@Repository
public interface EvidencijaRepository extends JpaRepository<Evidencija, Long>, JpaSpecificationExecutor<Evidencija> {
    default Optional<Evidencija> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Evidencija> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Evidencija> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select evidencija from Evidencija evidencija left join fetch evidencija.korisnikIzvrsio left join fetch evidencija.korisnikIspravio left join fetch evidencija.raspored left join fetch evidencija.vrstaUsluge left join fetch evidencija.statusEvidencije",
        countQuery = "select count(evidencija) from Evidencija evidencija"
    )
    Page<Evidencija> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select evidencija from Evidencija evidencija left join fetch evidencija.korisnikIzvrsio left join fetch evidencija.korisnikIspravio left join fetch evidencija.raspored left join fetch evidencija.vrstaUsluge left join fetch evidencija.statusEvidencije"
    )
    List<Evidencija> findAllWithToOneRelationships();

    @Query(
        "select evidencija from Evidencija evidencija left join fetch evidencija.korisnikIzvrsio left join fetch evidencija.korisnikIspravio left join fetch evidencija.raspored left join fetch evidencija.vrstaUsluge left join fetch evidencija.statusEvidencije where evidencija.id =:id"
    )
    Optional<Evidencija> findOneWithToOneRelationships(@Param("id") Long id);
}
