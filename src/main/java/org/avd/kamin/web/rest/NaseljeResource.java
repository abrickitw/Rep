package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.NaseljeRepository;
import org.avd.kamin.service.NaseljeQueryService;
import org.avd.kamin.service.NaseljeService;
import org.avd.kamin.service.criteria.NaseljeCriteria;
import org.avd.kamin.service.dto.NaseljeDTO;
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
 * REST controller for managing {@link org.avd.kamin.domain.Naselje}.
 */
@RestController
@RequestMapping("/api/naseljes")
public class NaseljeResource {

    private static final Logger log = LoggerFactory.getLogger(NaseljeResource.class);

    private static final String ENTITY_NAME = "naselje";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NaseljeService naseljeService;

    private final NaseljeRepository naseljeRepository;

    private final NaseljeQueryService naseljeQueryService;

    public NaseljeResource(NaseljeService naseljeService, NaseljeRepository naseljeRepository, NaseljeQueryService naseljeQueryService) {
        this.naseljeService = naseljeService;
        this.naseljeRepository = naseljeRepository;
        this.naseljeQueryService = naseljeQueryService;
    }

    /**
     * {@code POST  /naseljes} : Create a new naselje.
     *
     * @param naseljeDTO the naseljeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new naseljeDTO, or with status {@code 400 (Bad Request)} if the naselje has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NaseljeDTO> createNaselje(@Valid @RequestBody NaseljeDTO naseljeDTO) throws URISyntaxException {
        log.debug("REST request to save Naselje : {}", naseljeDTO);
        if (naseljeDTO.getId() != null) {
            throw new BadRequestAlertException("A new naselje cannot already have an ID", ENTITY_NAME, "idexists");
        }
        naseljeDTO = naseljeService.save(naseljeDTO);
        return ResponseEntity.created(new URI("/api/naseljes/" + naseljeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, naseljeDTO.getId().toString()))
            .body(naseljeDTO);
    }

    /**
     * {@code PUT  /naseljes/:id} : Updates an existing naselje.
     *
     * @param id the id of the naseljeDTO to save.
     * @param naseljeDTO the naseljeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated naseljeDTO,
     * or with status {@code 400 (Bad Request)} if the naseljeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the naseljeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NaseljeDTO> updateNaselje(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NaseljeDTO naseljeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Naselje : {}, {}", id, naseljeDTO);
        if (naseljeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, naseljeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!naseljeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        naseljeDTO = naseljeService.update(naseljeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, naseljeDTO.getId().toString()))
            .body(naseljeDTO);
    }

    /**
     * {@code PATCH  /naseljes/:id} : Partial updates given fields of an existing naselje, field will ignore if it is null
     *
     * @param id the id of the naseljeDTO to save.
     * @param naseljeDTO the naseljeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated naseljeDTO,
     * or with status {@code 400 (Bad Request)} if the naseljeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the naseljeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the naseljeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NaseljeDTO> partialUpdateNaselje(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NaseljeDTO naseljeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Naselje partially : {}, {}", id, naseljeDTO);
        if (naseljeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, naseljeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!naseljeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NaseljeDTO> result = naseljeService.partialUpdate(naseljeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, naseljeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /naseljes} : get all the naseljes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of naseljes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NaseljeDTO>> getAllNaseljes(
        NaseljeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Naseljes by criteria: {}", criteria);

        Page<NaseljeDTO> page = naseljeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /naseljes/count} : count all the naseljes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNaseljes(NaseljeCriteria criteria) {
        log.debug("REST request to count Naseljes by criteria: {}", criteria);
        return ResponseEntity.ok().body(naseljeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /naseljes/:id} : get the "id" naselje.
     *
     * @param id the id of the naseljeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the naseljeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NaseljeDTO> getNaselje(@PathVariable("id") Long id) {
        log.debug("REST request to get Naselje : {}", id);
        Optional<NaseljeDTO> naseljeDTO = naseljeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(naseljeDTO);
    }

    /**
     * {@code DELETE  /naseljes/:id} : delete the "id" naselje.
     *
     * @param id the id of the naseljeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNaselje(@PathVariable("id") Long id) {
        log.debug("REST request to delete Naselje : {}", id);
        naseljeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
