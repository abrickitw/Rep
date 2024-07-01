package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.StatusEvidencije}.
 */
public interface StatusEvidencijeService {
    /**
     * Save a statusEvidencije.
     *
     * @param statusEvidencijeDTO the entity to save.
     * @return the persisted entity.
     */
    StatusEvidencijeDTO save(StatusEvidencijeDTO statusEvidencijeDTO);

    /**
     * Updates a statusEvidencije.
     *
     * @param statusEvidencijeDTO the entity to update.
     * @return the persisted entity.
     */
    StatusEvidencijeDTO update(StatusEvidencijeDTO statusEvidencijeDTO);

    /**
     * Partially updates a statusEvidencije.
     *
     * @param statusEvidencijeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StatusEvidencijeDTO> partialUpdate(StatusEvidencijeDTO statusEvidencijeDTO);

    /**
     * Get the "id" statusEvidencije.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StatusEvidencijeDTO> findOne(Long id);

    /**
     * Delete the "id" statusEvidencije.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
