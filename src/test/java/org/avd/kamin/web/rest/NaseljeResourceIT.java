package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.NaseljeAsserts.*;
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
import org.avd.kamin.repository.NaseljeRepository;
import org.avd.kamin.service.NaseljeService;
import org.avd.kamin.service.dto.NaseljeDTO;
import org.avd.kamin.service.mapper.NaseljeMapper;
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
 * Integration tests for the {@link NaseljeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NaseljeResourceIT {

    private static final String DEFAULT_NASELJE_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NASELJE_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/naseljes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NaseljeRepository naseljeRepository;

    @Mock
    private NaseljeRepository naseljeRepositoryMock;

    @Autowired
    private NaseljeMapper naseljeMapper;

    @Mock
    private NaseljeService naseljeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNaseljeMockMvc;

    private Naselje naselje;

    private Naselje insertedNaselje;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Naselje createEntity(EntityManager em) {
        Naselje naselje = new Naselje().naseljeNaziv(DEFAULT_NASELJE_NAZIV);
        return naselje;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Naselje createUpdatedEntity(EntityManager em) {
        Naselje naselje = new Naselje().naseljeNaziv(UPDATED_NASELJE_NAZIV);
        return naselje;
    }

    @BeforeEach
    public void initTest() {
        naselje = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedNaselje != null) {
            naseljeRepository.delete(insertedNaselje);
            insertedNaselje = null;
        }
    }

    @Test
    @Transactional
    void createNaselje() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);
        var returnedNaseljeDTO = om.readValue(
            restNaseljeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(naseljeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NaseljeDTO.class
        );

        // Validate the Naselje in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNaselje = naseljeMapper.toEntity(returnedNaseljeDTO);
        assertNaseljeUpdatableFieldsEquals(returnedNaselje, getPersistedNaselje(returnedNaselje));

        insertedNaselje = returnedNaselje;
    }

    @Test
    @Transactional
    void createNaseljeWithExistingId() throws Exception {
        // Create the Naselje with an existing ID
        naselje.setId(1L);
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNaseljeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(naseljeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNaseljeNazivIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        naselje.setNaseljeNaziv(null);

        // Create the Naselje, which fails.
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        restNaseljeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(naseljeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNaseljes() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naselje.getId().intValue())))
            .andExpect(jsonPath("$.[*].naseljeNaziv").value(hasItem(DEFAULT_NASELJE_NAZIV)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNaseljesWithEagerRelationshipsIsEnabled() throws Exception {
        when(naseljeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNaseljeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(naseljeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNaseljesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(naseljeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNaseljeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(naseljeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNaselje() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get the naselje
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL_ID, naselje.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(naselje.getId().intValue()))
            .andExpect(jsonPath("$.naseljeNaziv").value(DEFAULT_NASELJE_NAZIV));
    }

    @Test
    @Transactional
    void getNaseljesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        Long id = naselje.getId();

        defaultNaseljeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNaseljeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNaseljeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNaseljesByNaseljeNazivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList where naseljeNaziv equals to
        defaultNaseljeFiltering("naseljeNaziv.equals=" + DEFAULT_NASELJE_NAZIV, "naseljeNaziv.equals=" + UPDATED_NASELJE_NAZIV);
    }

    @Test
    @Transactional
    void getAllNaseljesByNaseljeNazivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList where naseljeNaziv in
        defaultNaseljeFiltering(
            "naseljeNaziv.in=" + DEFAULT_NASELJE_NAZIV + "," + UPDATED_NASELJE_NAZIV,
            "naseljeNaziv.in=" + UPDATED_NASELJE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllNaseljesByNaseljeNazivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList where naseljeNaziv is not null
        defaultNaseljeFiltering("naseljeNaziv.specified=true", "naseljeNaziv.specified=false");
    }

    @Test
    @Transactional
    void getAllNaseljesByNaseljeNazivContainsSomething() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList where naseljeNaziv contains
        defaultNaseljeFiltering("naseljeNaziv.contains=" + DEFAULT_NASELJE_NAZIV, "naseljeNaziv.contains=" + UPDATED_NASELJE_NAZIV);
    }

    @Test
    @Transactional
    void getAllNaseljesByNaseljeNazivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        // Get all the naseljeList where naseljeNaziv does not contain
        defaultNaseljeFiltering(
            "naseljeNaziv.doesNotContain=" + UPDATED_NASELJE_NAZIV,
            "naseljeNaziv.doesNotContain=" + DEFAULT_NASELJE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllNaseljesByGradIsEqualToSomething() throws Exception {
        Grad grad;
        if (TestUtil.findAll(em, Grad.class).isEmpty()) {
            naseljeRepository.saveAndFlush(naselje);
            grad = GradResourceIT.createEntity(em);
        } else {
            grad = TestUtil.findAll(em, Grad.class).get(0);
        }
        em.persist(grad);
        em.flush();
        naselje.setGrad(grad);
        naseljeRepository.saveAndFlush(naselje);
        Long gradId = grad.getId();
        // Get all the naseljeList where grad equals to gradId
        defaultNaseljeShouldBeFound("gradId.equals=" + gradId);

        // Get all the naseljeList where grad equals to (gradId + 1)
        defaultNaseljeShouldNotBeFound("gradId.equals=" + (gradId + 1));
    }

    private void defaultNaseljeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNaseljeShouldBeFound(shouldBeFound);
        defaultNaseljeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNaseljeShouldBeFound(String filter) throws Exception {
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naselje.getId().intValue())))
            .andExpect(jsonPath("$.[*].naseljeNaziv").value(hasItem(DEFAULT_NASELJE_NAZIV)));

        // Check, that the count call also returns 1
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNaseljeShouldNotBeFound(String filter) throws Exception {
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNaseljeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNaselje() throws Exception {
        // Get the naselje
        restNaseljeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNaselje() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the naselje
        Naselje updatedNaselje = naseljeRepository.findById(naselje.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNaselje are not directly saved in db
        em.detach(updatedNaselje);
        updatedNaselje.naseljeNaziv(UPDATED_NASELJE_NAZIV);
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(updatedNaselje);

        restNaseljeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, naseljeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNaseljeToMatchAllProperties(updatedNaselje);
    }

    @Test
    @Transactional
    void putNonExistingNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, naseljeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(naseljeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNaseljeWithPatch() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the naselje using partial update
        Naselje partialUpdatedNaselje = new Naselje();
        partialUpdatedNaselje.setId(naselje.getId());

        partialUpdatedNaselje.naseljeNaziv(UPDATED_NASELJE_NAZIV);

        restNaseljeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNaselje.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNaselje))
            )
            .andExpect(status().isOk());

        // Validate the Naselje in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNaseljeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNaselje, naselje), getPersistedNaselje(naselje));
    }

    @Test
    @Transactional
    void fullUpdateNaseljeWithPatch() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the naselje using partial update
        Naselje partialUpdatedNaselje = new Naselje();
        partialUpdatedNaselje.setId(naselje.getId());

        partialUpdatedNaselje.naseljeNaziv(UPDATED_NASELJE_NAZIV);

        restNaseljeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNaselje.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNaselje))
            )
            .andExpect(status().isOk());

        // Validate the Naselje in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNaseljeUpdatableFieldsEquals(partialUpdatedNaselje, getPersistedNaselje(partialUpdatedNaselje));
    }

    @Test
    @Transactional
    void patchNonExistingNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, naseljeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNaselje() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        naselje.setId(longCount.incrementAndGet());

        // Create the Naselje
        NaseljeDTO naseljeDTO = naseljeMapper.toDto(naselje);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaseljeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(naseljeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Naselje in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNaselje() throws Exception {
        // Initialize the database
        insertedNaselje = naseljeRepository.saveAndFlush(naselje);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the naselje
        restNaseljeMockMvc
            .perform(delete(ENTITY_API_URL_ID, naselje.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return naseljeRepository.count();
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

    protected Naselje getPersistedNaselje(Naselje naselje) {
        return naseljeRepository.findById(naselje.getId()).orElseThrow();
    }

    protected void assertPersistedNaseljeToMatchAllProperties(Naselje expectedNaselje) {
        assertNaseljeAllPropertiesEquals(expectedNaselje, getPersistedNaselje(expectedNaselje));
    }

    protected void assertPersistedNaseljeToMatchUpdatableProperties(Naselje expectedNaselje) {
        assertNaseljeAllUpdatablePropertiesEquals(expectedNaselje, getPersistedNaselje(expectedNaselje));
    }
}
