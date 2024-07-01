package org.avd.kamin.service;

import java.util.Optional;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.avd.kamin.domain.Evidencija}.
 */
public interface EvidencijaService {
    /**
     * Save a evidencija.
     *
     * @param evidencijaDTO the entity to save.
     * @return the persisted entity.
     */
    EvidencijaDTO save(EvidencijaDTO evidencijaDTO);

    /**
     * Updates a evidencija.
     *
     * @param evidencijaDTO the entity to update.
     * @return the persisted entity.
     */
    EvidencijaDTO update(EvidencijaDTO evidencijaDTO);

    /**
     * Partially updates a evidencija.
     *
     * @param evidencijaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvidencijaDTO> partialUpdate(EvidencijaDTO evidencijaDTO);

    /**
     * Get all the evidencijas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvidencijaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" evidencija.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvidencijaDTO> findOne(Long id);

    /**
     * Delete the "id" evidencija.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
