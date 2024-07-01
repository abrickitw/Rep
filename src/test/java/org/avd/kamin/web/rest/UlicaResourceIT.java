package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.UlicaAsserts.*;
import static org.avd.kamin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.avd.kamin.IntegrationTest;
import org.avd.kamin.domain.Grad;
import org.avd.kamin.domain.Naselje;
import org.avd.kamin.domain.Ulica;
import org.avd.kamin.repository.UlicaRepository;
import org.avd.kamin.service.UlicaService;
import org.avd.kamin.service.dto.UlicaDTO;
import org.avd.kamin.service.mapper.UlicaMapper;
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
 * Integration tests for the {@link UlicaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UlicaResourceIT {

    private static final String DEFAULT_ULICA_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_ULICA_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ulicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UlicaRepository ulicaRepository;

    @Mock
    private UlicaRepository ulicaRepositoryMock;

    @Autowired
    private UlicaMapper ulicaMapper;

    @Mock
    private UlicaService ulicaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUlicaMockMvc;

    private Ulica ulica;

    private Ulica insertedUlica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ulica createEntity(EntityManager em) {
        Ulica ulica = new Ulica().ulicaNaziv(DEFAULT_ULICA_NAZIV);
        return ulica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ulica createUpdatedEntity(EntityManager em) {
        Ulica ulica = new Ulica().ulicaNaziv(UPDATED_ULICA_NAZIV);
        return ulica;
    }

    @BeforeEach
    public void initTest() {
        ulica = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUlica != null) {
            ulicaRepository.delete(insertedUlica);
            insertedUlica = null;
        }
    }

    @Test
    @Transactional
    void createUlica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);
        var returnedUlicaDTO = om.readValue(
            restUlicaMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ulicaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UlicaDTO.class
        );

        // Validate the Ulica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUlica = ulicaMapper.toEntity(returnedUlicaDTO);
        assertUlicaUpdatableFieldsEquals(returnedUlica, getPersistedUlica(returnedUlica));

        insertedUlica = returnedUlica;
    }

    @Test
    @Transactional
    void createUlicaWithExistingId() throws Exception {
        // Create the Ulica with an existing ID
        ulica.setId(1L);
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUlicaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ulicaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUlicaNazivIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ulica.setUlicaNaziv(null);

        // Create the Ulica, which fails.
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        restUlicaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ulicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUlicas() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ulica.getId().intValue())))
            .andExpect(jsonPath("$.[*].ulicaNaziv").value(hasItem(DEFAULT_ULICA_NAZIV)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUlicasWithEagerRelationshipsIsEnabled() throws Exception {
        when(ulicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUlicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ulicaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUlicasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ulicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUlicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(ulicaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUlica() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get the ulica
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL_ID, ulica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ulica.getId().intValue()))
            .andExpect(jsonPath("$.ulicaNaziv").value(DEFAULT_ULICA_NAZIV));
    }

    @Test
    @Transactional
    void getUlicasByIdFiltering() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        Long id = ulica.getId();

        defaultUlicaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUlicaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUlicaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUlicasByUlicaNazivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList where ulicaNaziv equals to
        defaultUlicaFiltering("ulicaNaziv.equals=" + DEFAULT_ULICA_NAZIV, "ulicaNaziv.equals=" + UPDATED_ULICA_NAZIV);
    }

    @Test
    @Transactional
    void getAllUlicasByUlicaNazivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList where ulicaNaziv in
        defaultUlicaFiltering("ulicaNaziv.in=" + DEFAULT_ULICA_NAZIV + "," + UPDATED_ULICA_NAZIV, "ulicaNaziv.in=" + UPDATED_ULICA_NAZIV);
    }

    @Test
    @Transactional
    void getAllUlicasByUlicaNazivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList where ulicaNaziv is not null
        defaultUlicaFiltering("ulicaNaziv.specified=true", "ulicaNaziv.specified=false");
    }

    @Test
    @Transactional
    void getAllUlicasByUlicaNazivContainsSomething() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList where ulicaNaziv contains
        defaultUlicaFiltering("ulicaNaziv.contains=" + DEFAULT_ULICA_NAZIV, "ulicaNaziv.contains=" + UPDATED_ULICA_NAZIV);
    }

    @Test
    @Transactional
    void getAllUlicasByUlicaNazivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        // Get all the ulicaList where ulicaNaziv does not contain
        defaultUlicaFiltering("ulicaNaziv.doesNotContain=" + UPDATED_ULICA_NAZIV, "ulicaNaziv.doesNotContain=" + DEFAULT_ULICA_NAZIV);
    }

    @Test
    @Transactional
    void getAllUlicasByGradIsEqualToSomething() throws Exception {
        Grad grad;
        if (TestUtil.findAll(em, Grad.class).isEmpty()) {
            ulicaRepository.saveAndFlush(ulica);
            grad = GradResourceIT.createEntity(em);
        } else {
            grad = TestUtil.findAll(em, Grad.class).get(0);
        }
        em.persist(grad);
        em.flush();
        ulica.setGrad(grad);
        ulicaRepository.saveAndFlush(ulica);
        Long gradId = grad.getId();
        // Get all the ulicaList where grad equals to gradId
        defaultUlicaShouldBeFound("gradId.equals=" + gradId);

        // Get all the ulicaList where grad equals to (gradId + 1)
        defaultUlicaShouldNotBeFound("gradId.equals=" + (gradId + 1));
    }

    @Test
    @Transactional
    void getAllUlicasByNaseljeIsEqualToSomething() throws Exception {
        Naselje naselje;
        if (TestUtil.findAll(em, Naselje.class).isEmpty()) {
            ulicaRepository.saveAndFlush(ulica);
            naselje = NaseljeResourceIT.createEntity(em);
        } else {
            naselje = TestUtil.findAll(em, Naselje.class).get(0);
        }
        em.persist(naselje);
        em.flush();
        ulica.setNaselje(naselje);
        ulicaRepository.saveAndFlush(ulica);
        Long naseljeId = naselje.getId();
        // Get all the ulicaList where naselje equals to naseljeId
        defaultUlicaShouldBeFound("naseljeId.equals=" + naseljeId);

        // Get all the ulicaList where naselje equals to (naseljeId + 1)
        defaultUlicaShouldNotBeFound("naseljeId.equals=" + (naseljeId + 1));
    }

    private void defaultUlicaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUlicaShouldBeFound(shouldBeFound);
        defaultUlicaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUlicaShouldBeFound(String filter) throws Exception {
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ulica.getId().intValue())))
            .andExpect(jsonPath("$.[*].ulicaNaziv").value(hasItem(DEFAULT_ULICA_NAZIV)));

        // Check, that the count call also returns 1
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUlicaShouldNotBeFound(String filter) throws Exception {
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUlicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUlica() throws Exception {
        // Get the ulica
        restUlicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUlica() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ulica
        Ulica updatedUlica = ulicaRepository.findById(ulica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUlica are not directly saved in db
        em.detach(updatedUlica);
        updatedUlica.ulicaNaziv(UPDATED_ULICA_NAZIV);
        UlicaDTO ulicaDTO = ulicaMapper.toDto(updatedUlica);

        restUlicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ulicaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ulicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUlicaToMatchAllProperties(updatedUlica);
    }

    @Test
    @Transactional
    void putNonExistingUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ulicaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ulicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ulicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ulicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUlicaWithPatch() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ulica using partial update
        Ulica partialUpdatedUlica = new Ulica();
        partialUpdatedUlica.setId(ulica.getId());

        partialUpdatedUlica.ulicaNaziv(UPDATED_ULICA_NAZIV);

        restUlicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUlica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUlica))
            )
            .andExpect(status().isOk());

        // Validate the Ulica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUlicaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUlica, ulica), getPersistedUlica(ulica));
    }

    @Test
    @Transactional
    void fullUpdateUlicaWithPatch() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ulica using partial update
        Ulica partialUpdatedUlica = new Ulica();
        partialUpdatedUlica.setId(ulica.getId());

        partialUpdatedUlica.ulicaNaziv(UPDATED_ULICA_NAZIV);

        restUlicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUlica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUlica))
            )
            .andExpect(status().isOk());

        // Validate the Ulica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUlicaUpdatableFieldsEquals(partialUpdatedUlica, getPersistedUlica(partialUpdatedUlica));
    }

    @Test
    @Transactional
    void patchNonExistingUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ulicaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ulicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ulicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUlica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ulica.setId(longCount.incrementAndGet());

        // Create the Ulica
        UlicaDTO ulicaDTO = ulicaMapper.toDto(ulica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUlicaMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ulicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ulica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUlica() throws Exception {
        // Initialize the database
        insertedUlica = ulicaRepository.saveAndFlush(ulica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ulica
        restUlicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ulica.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ulicaRepository.count();
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

    protected Ulica getPersistedUlica(Ulica ulica) {
        return ulicaRepository.findById(ulica.getId()).orElseThrow();
    }

    protected void assertPersistedUlicaToMatchAllProperties(Ulica expectedUlica) {
        assertUlicaAllPropertiesEquals(expectedUlica, getPersistedUlica(expectedUlica));
    }

    protected void assertPersistedUlicaToMatchUpdatableProperties(Ulica expectedUlica) {
        assertUlicaAllUpdatablePropertiesEquals(expectedUlica, getPersistedUlica(expectedUlica));
    }
}
