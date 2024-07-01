package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.VrstaUslugeRepository;
import org.avd.kamin.service.VrstaUslugeQueryService;
import org.avd.kamin.service.VrstaUslugeService;
import org.avd.kamin.service.criteria.VrstaUslugeCriteria;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
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
 * REST controller for managing {@link org.avd.kamin.domain.VrstaUsluge}.
 */
@RestController
@RequestMapping("/api/vrsta-usluges")
public class VrstaUslugeResource {

    private static final Logger log = LoggerFactory.getLogger(VrstaUslugeResource.class);

    private static final String ENTITY_NAME = "vrstaUsluge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VrstaUslugeService vrstaUslugeService;

    private final VrstaUslugeRepository vrstaUslugeRepository;

    private final VrstaUslugeQueryService vrstaUslugeQueryService;

    public VrstaUslugeResource(
        VrstaUslugeService vrstaUslugeService,
        VrstaUslugeRepository vrstaUslugeRepository,
        VrstaUslugeQueryService vrstaUslugeQueryService
    ) {
        this.vrstaUslugeService = vrstaUslugeService;
        this.vrstaUslugeRepository = vrstaUslugeRepository;
        this.vrstaUslugeQueryService = vrstaUslugeQueryService;
    }

    /**
     * {@code POST  /vrsta-usluges} : Create a new vrstaUsluge.
     *
     * @param vrstaUslugeDTO the vrstaUslugeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vrstaUslugeDTO, or with status {@code 400 (Bad Request)} if the vrstaUsluge has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VrstaUslugeDTO> createVrstaUsluge(@Valid @RequestBody VrstaUslugeDTO vrstaUslugeDTO) throws URISyntaxException {
        log.debug("REST request to save VrstaUsluge : {}", vrstaUslugeDTO);
        if (vrstaUslugeDTO.getId() != null) {
            throw new BadRequestAlertException("A new vrstaUsluge cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vrstaUslugeDTO = vrstaUslugeService.save(vrstaUslugeDTO);
        return ResponseEntity.created(new URI("/api/vrsta-usluges/" + vrstaUslugeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vrstaUslugeDTO.getId().toString()))
            .body(vrstaUslugeDTO);
    }

    /**
     * {@code PUT  /vrsta-usluges/:id} : Updates an existing vrstaUsluge.
     *
     * @param id the id of the vrstaUslugeDTO to save.
     * @param vrstaUslugeDTO the vrstaUslugeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vrstaUslugeDTO,
     * or with status {@code 400 (Bad Request)} if the vrstaUslugeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vrstaUslugeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VrstaUslugeDTO> updateVrstaUsluge(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VrstaUslugeDTO vrstaUslugeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VrstaUsluge : {}, {}", id, vrstaUslugeDTO);
        if (vrstaUslugeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vrstaUslugeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vrstaUslugeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vrstaUslugeDTO = vrstaUslugeService.update(vrstaUslugeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vrstaUslugeDTO.getId().toString()))
            .body(vrstaUslugeDTO);
    }

    /**
     * {@code PATCH  /vrsta-usluges/:id} : Partial updates given fields of an existing vrstaUsluge, field will ignore if it is null
     *
     * @param id the id of the vrstaUslugeDTO to save.
     * @param vrstaUslugeDTO the vrstaUslugeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vrstaUslugeDTO,
     * or with status {@code 400 (Bad Request)} if the vrstaUslugeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vrstaUslugeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vrstaUslugeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VrstaUslugeDTO> partialUpdateVrstaUsluge(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VrstaUslugeDTO vrstaUslugeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VrstaUsluge partially : {}, {}", id, vrstaUslugeDTO);
        if (vrstaUslugeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vrstaUslugeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vrstaUslugeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VrstaUslugeDTO> result = vrstaUslugeService.partialUpdate(vrstaUslugeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vrstaUslugeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vrsta-usluges} : get all the vrstaUsluges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vrstaUsluges in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VrstaUslugeDTO>> getAllVrstaUsluges(
        VrstaUslugeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get VrstaUsluges by criteria: {}", criteria);

        Page<VrstaUslugeDTO> page = vrstaUslugeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vrsta-usluges/count} : count all the vrstaUsluges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVrstaUsluges(VrstaUslugeCriteria criteria) {
        log.debug("REST request to count VrstaUsluges by criteria: {}", criteria);
        return ResponseEntity.ok().body(vrstaUslugeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vrsta-usluges/:id} : get the "id" vrstaUsluge.
     *
     * @param id the id of the vrstaUslugeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vrstaUslugeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VrstaUslugeDTO> getVrstaUsluge(@PathVariable("id") Long id) {
        log.debug("REST request to get VrstaUsluge : {}", id);
        Optional<VrstaUslugeDTO> vrstaUslugeDTO = vrstaUslugeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vrstaUslugeDTO);
    }

    /**
     * {@code DELETE  /vrsta-usluges/:id} : delete the "id" vrstaUsluge.
     *
     * @param id the id of the vrstaUslugeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVrstaUsluge(@PathVariable("id") Long id) {
        log.debug("REST request to delete VrstaUsluge : {}", id);
        vrstaUslugeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
