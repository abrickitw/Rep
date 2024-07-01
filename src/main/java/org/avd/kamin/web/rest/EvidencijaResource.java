package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.EvidencijaRepository;
import org.avd.kamin.service.EvidencijaQueryService;
import org.avd.kamin.service.EvidencijaService;
import org.avd.kamin.service.criteria.EvidencijaCriteria;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.avd.kamin.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.avd.kamin.domain.Evidencija}.
 */
@RestController
@RequestMapping("/api/evidencijas")
public class EvidencijaResource {

    private static final Logger log = LoggerFactory.getLogger(EvidencijaResource.class);

    private static final String ENTITY_NAME = "evidencija";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvidencijaService evidencijaService;

    private final EvidencijaRepository evidencijaRepository;

    private final EvidencijaQueryService evidencijaQueryService;

    public EvidencijaResource(
        EvidencijaService evidencijaService,
        EvidencijaRepository evidencijaRepository,
        EvidencijaQueryService evidencijaQueryService
    ) {
        this.evidencijaService = evidencijaService;
        this.evidencijaRepository = evidencijaRepository;
        this.evidencijaQueryService = evidencijaQueryService;
    }

    /**
     * {@code POST  /evidencijas} : Create a new evidencija.
     *
     * @param evidencijaDTO the evidencijaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evidencijaDTO, or with status {@code 400 (Bad Request)} if the evidencija has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EvidencijaDTO> createEvidencija(@Valid @RequestBody EvidencijaDTO evidencijaDTO) throws URISyntaxException {
        log.debug("REST request to save Evidencija : {}", evidencijaDTO);
        if (evidencijaDTO.getId() != null) {
            throw new BadRequestAlertException("A new evidencija cannot already have an ID", ENTITY_NAME, "idexists");
        }
        evidencijaDTO = evidencijaService.save(evidencijaDTO);
        return ResponseEntity.created(new URI("/api/evidencijas/" + evidencijaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, evidencijaDTO.getId().toString()))
            .body(evidencijaDTO);
    }

    /**
     * {@code PUT  /evidencijas/:id} : Updates an existing evidencija.
     *
     * @param id the id of the evidencijaDTO to save.
     * @param evidencijaDTO the evidencijaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evidencijaDTO,
     * or with status {@code 400 (Bad Request)} if the evidencijaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evidencijaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EvidencijaDTO> updateEvidencija(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EvidencijaDTO evidencijaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Evidencija : {}, {}", id, evidencijaDTO);
        if (evidencijaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evidencijaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evidencijaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        evidencijaDTO = evidencijaService.update(evidencijaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evidencijaDTO.getId().toString()))
            .body(evidencijaDTO);
    }

    /**
     * {@code PATCH  /evidencijas/:id} : Partial updates given fields of an existing evidencija, field will ignore if it is null
     *
     * @param id the id of the evidencijaDTO to save.
     * @param evidencijaDTO the evidencijaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evidencijaDTO,
     * or with status {@code 400 (Bad Request)} if the evidencijaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the evidencijaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the evidencijaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvidencijaDTO> partialUpdateEvidencija(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EvidencijaDTO evidencijaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Evidencija partially : {}, {}", id, evidencijaDTO);
        if (evidencijaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evidencijaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evidencijaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvidencijaDTO> result = evidencijaService.partialUpdate(evidencijaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evidencijaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /evidencijas} : get all the evidencijas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evidencijas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EvidencijaDTO>> getAllEvidencijas(
        EvidencijaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Evidencijas by criteria: {}", criteria);

        Page<EvidencijaDTO> page = evidencijaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /evidencijas/count} : count all the evidencijas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEvidencijas(EvidencijaCriteria criteria) {
        log.debug("REST request to count Evidencijas by criteria: {}", criteria);
        return ResponseEntity.ok().body(evidencijaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /evidencijas/:id} : get the "id" evidencija.
     *
     * @param id the id of the evidencijaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evidencijaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EvidencijaDTO> getEvidencija(@PathVariable("id") Long id) {
        log.debug("REST request to get Evidencija : {}", id);
        Optional<EvidencijaDTO> evidencijaDTO = evidencijaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evidencijaDTO);
    }

    /**
     * {@code DELETE  /evidencijas/:id} : delete the "id" evidencija.
     *
     * @param id the id of the evidencijaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvidencija(@PathVariable("id") Long id) {
        log.debug("REST request to delete Evidencija : {}", id);
        evidencijaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
