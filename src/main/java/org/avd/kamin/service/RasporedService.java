package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.RasporedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.Raspored}.
 */
public interface RasporedService {
    /**
     * Save a raspored.
     *
     * @param rasporedDTO the entity to save.
     * @return the persisted entity.
     */
    RasporedDTO save(RasporedDTO rasporedDTO);

    /**
     * Updates a raspored.
     *
     * @param rasporedDTO the entity to update.
     * @return the persisted entity.
     */
    RasporedDTO update(RasporedDTO rasporedDTO);

    /**
     * Partially updates a raspored.
     *
     * @param rasporedDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RasporedDTO> partialUpdate(RasporedDTO rasporedDTO);

    /**
     * Get all the rasporeds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RasporedDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" raspored.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RasporedDTO> findOne(Long id);

    /**
     * Delete the "id" raspored.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
