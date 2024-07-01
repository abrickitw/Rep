package org.avd.kamin.service;

import java.util.List;
import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.repository.StatusEvidencijeRepository;
import org.avd.kamin.service.criteria.StatusEvidencijeCriteria;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.avd.kamin.service.mapper.StatusEvidencijeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link StatusEvidencije} entities in the database.
 * The main input is a {@link StatusEvidencijeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StatusEvidencijeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StatusEvidencijeQueryService extends QueryService<StatusEvidencije> {

    private static final Logger log = LoggerFactory.getLogger(StatusEvidencijeQueryService.class);

    private final StatusEvidencijeRepository statusEvidencijeRepository;

    private final StatusEvidencijeMapper statusEvidencijeMapper;

    public StatusEvidencijeQueryService(
        StatusEvidencijeRepository statusEvidencijeRepository,
        StatusEvidencijeMapper statusEvidencijeMapper
    ) {
        this.statusEvidencijeRepository = statusEvidencijeRepository;
        this.statusEvidencijeMapper = statusEvidencijeMapper;
    }

    /**
     * Return a {@link List} of {@link StatusEvidencijeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StatusEvidencijeDTO> findByCriteria(StatusEvidencijeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StatusEvidencije> specification = createSpecification(criteria);
        return statusEvidencijeMapper.toDto(statusEvidencijeRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StatusEvidencijeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StatusEvidencije> specification = createSpecification(criteria);
        return statusEvidencijeRepository.count(specification);
    }

    /**
     * Function to convert {@link StatusEvidencijeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StatusEvidencije> createSpecification(StatusEvidencijeCriteria criteria) {
        Specification<StatusEvidencije> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StatusEvidencije_.id));
            }
            if (criteria.getStatusEvidencijeNaziv() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getStatusEvidencijeNaziv(), StatusEvidencije_.statusEvidencijeNaziv)
                );
            }
        }
        return specification;
    }
}
