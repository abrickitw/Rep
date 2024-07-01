package org.avd.kamin.service;

import jakarta.persistence.criteria.JoinType;
import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.repository.UlicaRepository;
import org.avd.kamin.service.criteria.UlicaCriteria;
import org.avd.kamin.service.dto.UlicaDTO;
import org.avd.kamin.service.mapper.UlicaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ulica} entities in the database.
 * The main input is a {@link UlicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UlicaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UlicaQueryService extends QueryService<Ulica> {

    private static final Logger log = LoggerFactory.getLogger(UlicaQueryService.class);

    private final UlicaRepository ulicaRepository;

    private final UlicaMapper ulicaMapper;

    public UlicaQueryService(UlicaRepository ulicaRepository, UlicaMapper ulicaMapper) {
        this.ulicaRepository = ulicaRepository;
        this.ulicaMapper = ulicaMapper;
    }

    /**
     * Return a {@link Page} of {@link UlicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UlicaDTO> findByCriteria(UlicaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ulica> specification = createSpecification(criteria);
        return ulicaRepository.findAll(specification, page).map(ulicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UlicaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ulica> specification = createSpecification(criteria);
        return ulicaRepository.count(specification);
    }

    /**
     * Function to convert {@link UlicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ulica> createSpecification(UlicaCriteria criteria) {
        Specification<Ulica> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ulica_.id));
            }
            if (criteria.getUlicaNaziv() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUlicaNaziv(), Ulica_.ulicaNaziv));
            }
            if (criteria.getGradId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGradId(), root -> root.join(Ulica_.grad, JoinType.LEFT).get(Grad_.id))
                );
            }
            if (criteria.getNaseljeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getNaseljeId(), root -> root.join(Ulica_.naselje, JoinType.LEFT).get(Naselje_.id))
                );
            }
        }
        return specification;
    }
}
