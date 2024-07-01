package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.VrstaUslugeDTO;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.VrstaUsluge}.
 */
public interface VrstaUslugeService {
    /**
     * Save a vrstaUsluge.
     *
     * @param vrstaUslugeDTO the entity to save.
     * @return the persisted entity.
     */
    VrstaUslugeDTO save(VrstaUslugeDTO vrstaUslugeDTO);

    /**
     * Updates a vrstaUsluge.
     *
     * @param vrstaUslugeDTO the entity to update.
     * @return the persisted entity.
     */
    VrstaUslugeDTO update(VrstaUslugeDTO vrstaUslugeDTO);

    /**
     * Partially updates a vrstaUsluge.
     *
     * @param vrstaUslugeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VrstaUslugeDTO> partialUpdate(VrstaUslugeDTO vrstaUslugeDTO);

    /**
     * Get the "id" vrstaUsluge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VrstaUslugeDTO> findOne(Long id);

    /**
     * Delete the "id" vrstaUsluge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
