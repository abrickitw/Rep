package org.avd.kamin.service;

import jakarta.persistence.criteria.JoinType;
import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.repository.NaseljeRepository;
import org.avd.kamin.service.criteria.NaseljeCriteria;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.avd.kamin.service.mapper.NaseljeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Naselje} entities in the database.
 * The main input is a {@link NaseljeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NaseljeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NaseljeQueryService extends QueryService<Naselje> {

    private static final Logger log = LoggerFactory.getLogger(NaseljeQueryService.class);

    private final NaseljeRepository naseljeRepository;

    private final NaseljeMapper naseljeMapper;

    public NaseljeQueryService(NaseljeRepository naseljeRepository, NaseljeMapper naseljeMapper) {
        this.naseljeRepository = naseljeRepository;
        this.naseljeMapper = naseljeMapper;
    }

    /**
     * Return a {@link Page} of {@link NaseljeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NaseljeDTO> findByCriteria(NaseljeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Naselje> specification = createSpecification(criteria);
        return naseljeRepository.findAll(specification, page).map(naseljeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NaseljeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Naselje> specification = createSpecification(criteria);
        return naseljeRepository.count(specification);
    }

    /**
     * Function to convert {@link NaseljeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Naselje> createSpecification(NaseljeCriteria criteria) {
        Specification<Naselje> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Naselje_.id));
            }
            if (criteria.getNaseljeNaziv() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNaseljeNaziv(), Naselje_.naseljeNaziv));
            }
            if (criteria.getGradId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGradId(), root -> root.join(Naselje_.grad, JoinType.LEFT).get(Grad_.id))
                );
            }
        }
        return specification;
    }
}
