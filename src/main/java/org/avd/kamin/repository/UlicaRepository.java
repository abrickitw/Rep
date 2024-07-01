package org.avd.kamin.repository;

import java.util.List;
import java.util.Optional;
import org.avd.kamin.domain.Ulica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ulica entity.
 */
@Repository
public interface UlicaRepository extends JpaRepository<Ulica, Long>, JpaSpecificationExecutor<Ulica> {
    default Optional<Ulica> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ulica> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ulica> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select ulica from Ulica ulica left join fetch ulica.grad left join fetch ulica.naselje",
        countQuery = "select count(ulica) from Ulica ulica"
    )
    Page<Ulica> findAllWithToOneRelationships(Pageable pageable);

    @Query("select ulica from Ulica ulica left join fetch ulica.grad left join fetch ulica.naselje")
    List<Ulica> findAllWithToOneRelationships();

    @Query("select ulica from Ulica ulica left join fetch ulica.grad left join fetch ulica.naselje where ulica.id =:id")
    Optional<Ulica> findOneWithToOneRelationships(@Param("id") Long id);
}
