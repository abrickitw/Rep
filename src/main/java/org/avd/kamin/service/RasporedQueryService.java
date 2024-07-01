package org.avd.kamin.service;

import jakarta.persistence.criteria.JoinType;
import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.repository.RasporedRepository;
import org.avd.kamin.service.criteria.RasporedCriteria;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.service.mapper.RasporedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Raspored} entities in the database.
 * The main input is a {@link RasporedCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RasporedDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RasporedQueryService extends QueryService<Raspored> {

    private static final Logger log = LoggerFactory.getLogger(RasporedQueryService.class);

    private final RasporedRepository rasporedRepository;

    private final RasporedMapper rasporedMapper;

    public RasporedQueryService(RasporedRepository rasporedRepository, RasporedMapper rasporedMapper) {
        this.rasporedRepository = rasporedRepository;
        this.rasporedMapper = rasporedMapper;
    }

    /**
     * Return a {@link Page} of {@link RasporedDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RasporedDTO> findByCriteria(RasporedCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Raspored> specification = createSpecification(criteria);
        return rasporedRepository.findAll(specification, page).map(rasporedMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RasporedCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Raspored> specification = createSpecification(criteria);
        return rasporedRepository.count(specification);
    }

    /**
     * Function to convert {@link RasporedCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Raspored> createSpecification(RasporedCriteria criteria) {
        Specification<Raspored> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Raspored_.id));
            }
            if (criteria.getDatumUsluge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumUsluge(), Raspored_.datumUsluge));
            }
            if (criteria.getGradId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGradId(), root -> root.join(Raspored_.grad, JoinType.LEFT).get(Grad_.id))
                );
            }
            if (criteria.getNaseljeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getNaseljeId(), root -> root.join(Raspored_.naselje, JoinType.LEFT).get(Naselje_.id))
                );
            }
            if (criteria.getUlicaId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUlicaId(), root -> root.join(Raspored_.ulica, JoinType.LEFT).get(Ulica_.id))
                );
            }
            if (criteria.getKorisnikKreiraoId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getKorisnikKreiraoId(),
                        root -> root.join(Raspored_.korisnikKreirao, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
