package org.avd.kamin.service;

import jakarta.persistence.criteria.JoinType;
import org.avd.kamin.domain.*; // for static metamodels
import org.avd.kamin.domain.Evidencija;
import org.avd.kamin.repository.EvidencijaRepository;
import org.avd.kamin.service.criteria.EvidencijaCriteria;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.avd.kamin.service.mapper.EvidencijaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Evidencija} entities in the database.
 * The main input is a {@link EvidencijaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EvidencijaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EvidencijaQueryService extends QueryService<Evidencija> {

    private static final Logger log = LoggerFactory.getLogger(EvidencijaQueryService.class);

    private final EvidencijaRepository evidencijaRepository;

    private final EvidencijaMapper evidencijaMapper;

    public EvidencijaQueryService(EvidencijaRepository evidencijaRepository, EvidencijaMapper evidencijaMapper) {
        this.evidencijaRepository = evidencijaRepository;
        this.evidencijaMapper = evidencijaMapper;
    }

    /**
     * Return a {@link Page} of {@link EvidencijaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EvidencijaDTO> findByCriteria(EvidencijaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Evidencija> specification = createSpecification(criteria);
        return evidencijaRepository.findAll(specification, page).map(evidencijaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EvidencijaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Evidencija> specification = createSpecification(criteria);
        return evidencijaRepository.count(specification);
    }

    /**
     * Function to convert {@link EvidencijaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Evidencija> createSpecification(EvidencijaCriteria criteria) {
        Specification<Evidencija> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Evidencija_.id));
            }
            if (criteria.getNazivEvidencija() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNazivEvidencija(), Evidencija_.nazivEvidencija));
            }
            if (criteria.getVrijemeUsluge() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVrijemeUsluge(), Evidencija_.vrijemeUsluge));
            }
            if (criteria.getKomentar() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKomentar(), Evidencija_.komentar));
            }
            if (criteria.getImeStanara() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImeStanara(), Evidencija_.imeStanara));
            }
            if (criteria.getPrezimeStanara() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrezimeStanara(), Evidencija_.prezimeStanara));
            }
            if (criteria.getKontaktStanara() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKontaktStanara(), Evidencija_.kontaktStanara));
            }
            if (criteria.getDatumIspravka() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatumIspravka(), Evidencija_.datumIspravka));
            }
            if (criteria.getKomentarIspravka() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKomentarIspravka(), Evidencija_.komentarIspravka));
            }
            if (criteria.getKucniBroj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKucniBroj(), Evidencija_.kucniBroj));
            }
            if (criteria.getKorisnikIzvrsioId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getKorisnikIzvrsioId(),
                        root -> root.join(Evidencija_.korisnikIzvrsio, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getKorisnikIspravioId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getKorisnikIspravioId(),
                        root -> root.join(Evidencija_.korisnikIspravio, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getRasporedId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRasporedId(), root -> root.join(Evidencija_.raspored, JoinType.LEFT).get(Raspored_.id))
                );
            }
            if (criteria.getVrstaUslugeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getVrstaUslugeId(),
                        root -> root.join(Evidencija_.vrstaUsluge, JoinType.LEFT).get(VrstaUsluge_.id)
                    )
                );
            }
            if (criteria.getStatusEvidencijeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getStatusEvidencijeId(),
                        root -> root.join(Evidencija_.statusEvidencije, JoinType.LEFT).get(StatusEvidencije_.id)
                    )
                );
            }
        }
        return specification;
    }
}
