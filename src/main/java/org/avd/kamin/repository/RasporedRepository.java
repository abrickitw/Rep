package org.avd.kamin.repository;

import java.util.List;
import java.util.Optional;
import org.avd.kamin.domain.Raspored;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Raspored entity.
 */
@Repository
public interface RasporedRepository extends JpaRepository<Raspored, Long>, JpaSpecificationExecutor<Raspored> {
    @Query("select raspored from Raspored raspored where raspored.korisnikKreirao.login = ?#{authentication.name}")
    List<Raspored> findByKorisnikKreiraoIsCurrentUser();

    default Optional<Raspored> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Raspored> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Raspored> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select raspored from Raspored raspored left join fetch raspored.grad left join fetch raspored.naselje left join fetch raspored.ulica left join fetch raspored.korisnikKreirao",
        countQuery = "select count(raspored) from Raspored raspored"
    )
    Page<Raspored> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select raspored from Raspored raspored left join fetch raspored.grad left join fetch raspored.naselje left join fetch raspored.ulica left join fetch raspored.korisnikKreirao"
    )
    List<Raspored> findAllWithToOneRelationships();

    @Query(
        "select raspored from Raspored raspored left join fetch raspored.grad left join fetch raspored.naselje left join fetch raspored.ulica left join fetch raspored.korisnikKreirao where raspored.id =:id"
    )
    Optional<Raspored> findOneWithToOneRelationships(@Param("id") Long id);
}
