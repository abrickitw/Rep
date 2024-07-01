package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.UlicaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.Ulica}.
 */
public interface UlicaService {
    /**
     * Save a ulica.
     *
     * @param ulicaDTO the entity to save.
     * @return the persisted entity.
     */
    UlicaDTO save(UlicaDTO ulicaDTO);

    /**
     * Updates a ulica.
     *
     * @param ulicaDTO the entity to update.
     * @return the persisted entity.
     */
    UlicaDTO update(UlicaDTO ulicaDTO);

    /**
     * Partially updates a ulica.
     *
     * @param ulicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UlicaDTO> partialUpdate(UlicaDTO ulicaDTO);

    /**
     * Get all the ulicas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UlicaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" ulica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UlicaDTO> findOne(Long id);

    /**
     * Delete the "id" ulica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
