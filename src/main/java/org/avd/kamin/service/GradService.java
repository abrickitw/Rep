package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.GradDTO;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.Grad}.
 */
public interface GradService {
    /**
     * Save a grad.
     *
     * @param gradDTO the entity to save.
     * @return the persisted entity.
     */
    GradDTO save(GradDTO gradDTO);

    /**
     * Updates a grad.
     *
     * @param gradDTO the entity to update.
     * @return the persisted entity.
     */
    GradDTO update(GradDTO gradDTO);

    /**
     * Partially updates a grad.
     *
     * @param gradDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GradDTO> partialUpdate(GradDTO gradDTO);

    /**
     * Get the "id" grad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GradDTO> findOne(Long id);

    /**
     * Delete the "id" grad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
