package org.avd.kamin.service;

import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.Grad;
import org.avd.kamin.repository.GradRepository;
import org.avd.kamin.service.criteria.GradCriteria;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.mapper.GradMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Grad} entities in the database.
 * The main input is a {@link GradCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GradDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GradQueryService extends QueryService<Grad> {

    private static final Logger log = LoggerFactory.getLogger(GradQueryService.class);

    private final GradRepository gradRepository;

    private final GradMapper gradMapper;

    public GradQueryService(GradRepository gradRepository, GradMapper gradMapper) {
        this.gradRepository = gradRepository;
        this.gradMapper = gradMapper;
    }

    /**
     * Return a {@link Page} of {@link GradDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GradDTO> findByCriteria(GradCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Grad> specification = createSpecification(criteria);
        return gradRepository.findAll(specification, page).map(gradMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GradCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Grad> specification = createSpecification(criteria);
        return gradRepository.count(specification);
    }

    /**
     * Function to convert {@link GradCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Grad> createSpecification(GradCriteria criteria) {
        Specification<Grad> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Grad_.id));
            }
            if (criteria.getGradNaziv() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGradNaziv(), Grad_.gradNaziv));
            }
        }
        return specification;
    }
}
