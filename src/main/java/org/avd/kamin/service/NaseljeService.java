package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.Naselje}.
 */
public interface NaseljeService {
    /**
     * Save a naselje.
     *
     * @param naseljeDTO the entity to save.
     * @return the persisted entity.
     */
    NaseljeDTO save(NaseljeDTO naseljeDTO);

    /**
     * Updates a naselje.
     *
     * @param naseljeDTO the entity to update.
     * @return the persisted entity.
     */
    NaseljeDTO update(NaseljeDTO naseljeDTO);

    /**
     * Partially updates a naselje.
     *
     * @param naseljeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NaseljeDTO> partialUpdate(NaseljeDTO naseljeDTO);

    /**
     * Get all the naseljes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NaseljeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" naselje.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NaseljeDTO> findOne(Long id);

    /**
     * Delete the "id" naselje.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
