package org.avd.kamin.repository;

import java.util.List;
import java.util.Optional;
import org.avd.kamin.domain.Naselje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Naselje entity.
 */
@Repository
public interface NaseljeRepository extends JpaRepository<Naselje, Long>, JpaSpecificationExecutor<Naselje> {
    default Optional<Naselje> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Naselje> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Naselje> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select naselje from Naselje naselje left join fetch naselje.grad",
        countQuery = "select count(naselje) from Naselje naselje"
    )
    Page<Naselje> findAllWithToOneRelationships(Pageable pageable);

    @Query("select naselje from Naselje naselje left join fetch naselje.grad")
    List<Naselje> findAllWithToOneRelationships();

    @Query("select naselje from Naselje naselje left join fetch naselje.grad where naselje.id =:id")
    Optional<Naselje> findOneWithToOneRelationships(@Param("id") Long id);
}
