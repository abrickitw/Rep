package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.RasporedAsserts.*;
import static org.avd.kamin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.avd.kamin.IntegrationTest;
import org.avd.kamin.domain.Grad;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.domain.User;
import org.avd.kamin.repository.RasporedRepository;
import org.avd.kamin.repository.UserRepository;
import org.avd.kamin.service.RasporedService;
import org.avd.kamin.service.dto.RasporedDTO;
import org.avd.kamin.service.mapper.RasporedMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RasporedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RasporedResourceIT {

    private static final LocalDate DEFAULT_DATUM_USLUGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_USLUGE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_USLUGE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/rasporeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RasporedRepository rasporedRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private RasporedRepository rasporedRepositoryMock;

    @Autowired
    private RasporedMapper rasporedMapper;

    @Mock
    private RasporedService rasporedServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRasporedMockMvc;

    private Raspored raspored;

    private Raspored insertedRaspored;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raspored createEntity(EntityManager em) {
        Raspored raspored = new Raspored().datumUsluge(DEFAULT_DATUM_USLUGE);
        return raspored;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raspored createUpdatedEntity(EntityManager em) {
        Raspored raspored = new Raspored().datumUsluge(UPDATED_DATUM_USLUGE);
        return raspored;
    }

    @BeforeEach
    public void initTest() {
        raspored = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRaspored != null) {
            rasporedRepository.delete(insertedRaspored);
            insertedRaspored = null;
        }
    }

    @Test
    @Transactional
    void createRaspored() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);
        var returnedRasporedDTO = om.readValue(
            restRasporedMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rasporedDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RasporedDTO.class
        );

        // Validate the Raspored in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRaspored = rasporedMapper.toEntity(returnedRasporedDTO);
        assertRasporedUpdatableFieldsEquals(returnedRaspored, getPersistedRaspored(returnedRaspored));

        insertedRaspored = returnedRaspored;
    }

    @Test
    @Transactional
    void createRasporedWithExistingId() throws Exception {
        // Create the Raspored with an existing ID
        raspored.setId(1L);
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRasporedMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rasporedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDatumUslugeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        raspored.setDatumUsluge(null);

        // Create the Raspored, which fails.
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        restRasporedMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rasporedDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRasporeds() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raspored.getId().intValue())))
            .andExpect(jsonPath("$.[*].datumUsluge").value(hasItem(DEFAULT_DATUM_USLUGE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRasporedsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rasporedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRasporedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rasporedServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRasporedsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rasporedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRasporedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(rasporedRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRaspored() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get the raspored
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL_ID, raspored.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raspored.getId().intValue()))
            .andExpect(jsonPath("$.datumUsluge").value(DEFAULT_DATUM_USLUGE.toString()));
    }

    @Test
    @Transactional
    void getRasporedsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        Long id = raspored.getId();

        defaultRasporedFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRasporedFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRasporedFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge equals to
        defaultRasporedFiltering("datumUsluge.equals=" + DEFAULT_DATUM_USLUGE, "datumUsluge.equals=" + UPDATED_DATUM_USLUGE);
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge in
        defaultRasporedFiltering(
            "datumUsluge.in=" + DEFAULT_DATUM_USLUGE + "," + UPDATED_DATUM_USLUGE,
            "datumUsluge.in=" + UPDATED_DATUM_USLUGE
        );
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge is not null
        defaultRasporedFiltering("datumUsluge.specified=true", "datumUsluge.specified=false");
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge is greater than or equal to
        defaultRasporedFiltering(
            "datumUsluge.greaterThanOrEqual=" + DEFAULT_DATUM_USLUGE,
            "datumUsluge.greaterThanOrEqual=" + UPDATED_DATUM_USLUGE
        );
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge is less than or equal to
        defaultRasporedFiltering(
            "datumUsluge.lessThanOrEqual=" + DEFAULT_DATUM_USLUGE,
            "datumUsluge.lessThanOrEqual=" + SMALLER_DATUM_USLUGE
        );
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge is less than
        defaultRasporedFiltering("datumUsluge.lessThan=" + UPDATED_DATUM_USLUGE, "datumUsluge.lessThan=" + DEFAULT_DATUM_USLUGE);
    }

    @Test
    @Transactional
    void getAllRasporedsByDatumUslugeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        // Get all the rasporedList where datumUsluge is greater than
        defaultRasporedFiltering("datumUsluge.greaterThan=" + SMALLER_DATUM_USLUGE, "datumUsluge.greaterThan=" + DEFAULT_DATUM_USLUGE);
    }

    @Test
    @Transactional
    void getAllRasporedsByGradIsEqualToSomething() throws Exception {
        Grad grad;
        if (TestUtil.findAll(em, Grad.class).isEmpty()) {
            rasporedRepository.saveAndFlush(raspored);
            grad = GradResourceIT.createEntity(em);
        } else {
            grad = TestUtil.findAll(em, Grad.class).get(0);
        }
        em.persist(grad);
        em.flush();
        raspored.setGrad(grad);
        rasporedRepository.saveAndFlush(raspored);
        Long gradId = grad.getId();
        // Get all the rasporedList where grad equals to gradId
        defaultRasporedShouldBeFound("gradId.equals=" + gradId);

        // Get all the rasporedList where grad equals to (gradId + 1)
        defaultRasporedShouldNotBeFound("gradId.equals=" + (gradId + 1));
    }

    @Test
    @Transactional
    void getAllRasporedsByNaseljeIsEqualToSomething() throws Exception {
        Naselje naselje;
        if (TestUtil.findAll(em, Naselje.class).isEmpty()) {
            rasporedRepository.saveAndFlush(raspored);
            naselje = NaseljeResourceIT.createEntity(em);
        } else {
            naselje = TestUtil.findAll(em, Naselje.class).get(0);
        }
        em.persist(naselje);
        em.flush();
        raspored.setNaselje(naselje);
        rasporedRepository.saveAndFlush(raspored);
        Long naseljeId = naselje.getId();
        // Get all the rasporedList where naselje equals to naseljeId
        defaultRasporedShouldBeFound("naseljeId.equals=" + naseljeId);

        // Get all the rasporedList where naselje equals to (naseljeId + 1)
        defaultRasporedShouldNotBeFound("naseljeId.equals=" + (naseljeId + 1));
    }

    @Test
    @Transactional
    void getAllRasporedsByUlicaIsEqualToSomething() throws Exception {
        Ulica ulica;
        if (TestUtil.findAll(em, Ulica.class).isEmpty()) {
            rasporedRepository.saveAndFlush(raspored);
            ulica = UlicaResourceIT.createEntity(em);
        } else {
            ulica = TestUtil.findAll(em, Ulica.class).get(0);
        }
        em.persist(ulica);
        em.flush();
        raspored.setUlica(ulica);
        rasporedRepository.saveAndFlush(raspored);
        Long ulicaId = ulica.getId();
        // Get all the rasporedList where ulica equals to ulicaId
        defaultRasporedShouldBeFound("ulicaId.equals=" + ulicaId);

        // Get all the rasporedList where ulica equals to (ulicaId + 1)
        defaultRasporedShouldNotBeFound("ulicaId.equals=" + (ulicaId + 1));
    }

    @Test
    @Transactional
    void getAllRasporedsByKorisnikKreiraoIsEqualToSomething() throws Exception {
        User korisnikKreirao;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            rasporedRepository.saveAndFlush(raspored);
            korisnikKreirao = UserResourceIT.createEntity(em);
        } else {
            korisnikKreirao = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(korisnikKreirao);
        em.flush();
        raspored.setKorisnikKreirao(korisnikKreirao);
        rasporedRepository.saveAndFlush(raspored);
        Long korisnikKreiraoId = korisnikKreirao.getId();
        // Get all the rasporedList where korisnikKreirao equals to korisnikKreiraoId
        defaultRasporedShouldBeFound("korisnikKreiraoId.equals=" + korisnikKreiraoId);

        // Get all the rasporedList where korisnikKreirao equals to (korisnikKreiraoId + 1)
        defaultRasporedShouldNotBeFound("korisnikKreiraoId.equals=" + (korisnikKreiraoId + 1));
    }

    private void defaultRasporedFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRasporedShouldBeFound(shouldBeFound);
        defaultRasporedShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRasporedShouldBeFound(String filter) throws Exception {
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raspored.getId().intValue())))
            .andExpect(jsonPath("$.[*].datumUsluge").value(hasItem(DEFAULT_DATUM_USLUGE.toString())));

        // Check, that the count call also returns 1
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRasporedShouldNotBeFound(String filter) throws Exception {
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRasporedMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRaspored() throws Exception {
        // Get the raspored
        restRasporedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRaspored() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raspored
        Raspored updatedRaspored = rasporedRepository.findById(raspored.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRaspored are not directly saved in db
        em.detach(updatedRaspored);
        updatedRaspored.datumUsluge(UPDATED_DATUM_USLUGE);
        RasporedDTO rasporedDTO = rasporedMapper.toDto(updatedRaspored);

        restRasporedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rasporedDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isOk());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRasporedToMatchAllProperties(updatedRaspored);
    }

    @Test
    @Transactional
    void putNonExistingRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rasporedDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rasporedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRasporedWithPatch() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raspored using partial update
        Raspored partialUpdatedRaspored = new Raspored();
        partialUpdatedRaspored.setId(raspored.getId());

        restRasporedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaspored.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaspored))
            )
            .andExpect(status().isOk());

        // Validate the Raspored in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRasporedUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRaspored, raspored), getPersistedRaspored(raspored));
    }

    @Test
    @Transactional
    void fullUpdateRasporedWithPatch() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raspored using partial update
        Raspored partialUpdatedRaspored = new Raspored();
        partialUpdatedRaspored.setId(raspored.getId());

        partialUpdatedRaspored.datumUsluge(UPDATED_DATUM_USLUGE);

        restRasporedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaspored.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaspored))
            )
            .andExpect(status().isOk());

        // Validate the Raspored in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRasporedUpdatableFieldsEquals(partialUpdatedRaspored, getPersistedRaspored(partialUpdatedRaspored));
    }

    @Test
    @Transactional
    void patchNonExistingRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rasporedDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaspored() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raspored.setId(longCount.incrementAndGet());

        // Create the Raspored
        RasporedDTO rasporedDTO = rasporedMapper.toDto(raspored);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRasporedMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rasporedDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raspored in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaspored() throws Exception {
        // Initialize the database
        insertedRaspored = rasporedRepository.saveAndFlush(raspored);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the raspored
        restRasporedMockMvc
            .perform(delete(ENTITY_API_URL_ID, raspored.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rasporedRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Raspored getPersistedRaspored(Raspored raspored) {
        return rasporedRepository.findById(raspored.getId()).orElseThrow();
    }

    protected void assertPersistedRasporedToMatchAllProperties(Raspored expectedRaspored) {
        assertRasporedAllPropertiesEquals(expectedRaspored, getPersistedRaspored(expectedRaspored));
    }

    protected void assertPersistedRasporedToMatchUpdatableProperties(Raspored expectedRaspored) {
        assertRasporedAllUpdatablePropertiesEquals(expectedRaspored, getPersistedRaspored(expectedRaspored));
    }
}
