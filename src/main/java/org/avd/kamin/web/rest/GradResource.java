package org.avd.kamin.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.avd.kamin.repository.GradRepository;
import org.avd.kamin.service.GradQueryService;
import org.avd.kamin.service.GradService;
import org.avd.kamin.service.criteria.GradCriteria;
import org.avd.kamin.service.dto.GradDTO;
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
 * REST controller for managing {@link org.avd.kamin.domain.Grad}.
 */
@RestController
@RequestMapping("/api/grads")
public class GradResource {

    private static final Logger log = LoggerFactory.getLogger(GradResource.class);

    private static final String ENTITY_NAME = "grad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GradService gradService;

    private final GradRepository gradRepository;

    private final GradQueryService gradQueryService;

    public GradResource(GradService gradService, GradRepository gradRepository, GradQueryService gradQueryService) {
        this.gradService = gradService;
        this.gradRepository = gradRepository;
        this.gradQueryService = gradQueryService;
    }

    /**
     * {@code POST  /grads} : Create a new grad.
     *
     * @param gradDTO the gradDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gradDTO, or with status {@code 400 (Bad Request)} if the grad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GradDTO> createGrad(@Valid @RequestBody GradDTO gradDTO) throws URISyntaxException {
        log.debug("REST request to save Grad : {}", gradDTO);
        if (gradDTO.getId() != null) {
            throw new BadRequestAlertException("A new grad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        gradDTO = gradService.save(gradDTO);
        return ResponseEntity.created(new URI("/api/grads/" + gradDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, gradDTO.getId().toString()))
            .body(gradDTO);
    }

    /**
     * {@code PUT  /grads/:id} : Updates an existing grad.
     *
     * @param id the id of the gradDTO to save.
     * @param gradDTO the gradDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradDTO,
     * or with status {@code 400 (Bad Request)} if the gradDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gradDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GradDTO> updateGrad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GradDTO gradDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Grad : {}, {}", id, gradDTO);
        if (gradDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        gradDTO = gradService.update(gradDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradDTO.getId().toString()))
            .body(gradDTO);
    }

    /**
     * {@code PATCH  /grads/:id} : Partial updates given fields of an existing grad, field will ignore if it is null
     *
     * @param id the id of the gradDTO to save.
     * @param gradDTO the gradDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradDTO,
     * or with status {@code 400 (Bad Request)} if the gradDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gradDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gradDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GradDTO> partialUpdateGrad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GradDTO gradDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Grad partially : {}, {}", id, gradDTO);
        if (gradDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GradDTO> result = gradService.partialUpdate(gradDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /grads} : get all the grads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of grads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GradDTO>> getAllGrads(
        GradCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Grads by criteria: {}", criteria);

        Page<GradDTO> page = gradQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grads/count} : count all the grads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGrads(GradCriteria criteria) {
        log.debug("REST request to count Grads by criteria: {}", criteria);
        return ResponseEntity.ok().body(gradQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grads/:id} : get the "id" grad.
     *
     * @param id the id of the gradDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gradDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GradDTO> getGrad(@PathVariable("id") Long id) {
        log.debug("REST request to get Grad : {}", id);
        Optional<GradDTO> gradDTO = gradService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gradDTO);
    }

    /**
     * {@code DELETE  /grads/:id} : delete the "id" grad.
     *
     * @param id the id of the gradDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrad(@PathVariable("id") Long id) {
        log.debug("REST request to delete Grad : {}", id);
        gradService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
