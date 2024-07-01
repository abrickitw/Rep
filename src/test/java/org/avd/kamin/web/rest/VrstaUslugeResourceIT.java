package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.VrstaUslugeAsserts.*;
import static org.avd.kamin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.avd.kamin.IntegrationTest;
import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.repository.VrstaUslugeRepository;
import org.avd.kamin.service.dto.VrstaUslugeDTO;
import org.avd.kamin.service.mapper.VrstaUslugeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VrstaUslugeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VrstaUslugeResourceIT {

    private static final String DEFAULT_VRSTA_USLUGE_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_VRSTA_USLUGE_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vrsta-usluges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VrstaUslugeRepository vrstaUslugeRepository;

    @Autowired
    private VrstaUslugeMapper vrstaUslugeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVrstaUslugeMockMvc;

    private VrstaUsluge vrstaUsluge;

    private VrstaUsluge insertedVrstaUsluge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VrstaUsluge createEntity(EntityManager em) {
        VrstaUsluge vrstaUsluge = new VrstaUsluge().vrstaUslugeNaziv(DEFAULT_VRSTA_USLUGE_NAZIV);
        return vrstaUsluge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VrstaUsluge createUpdatedEntity(EntityManager em) {
        VrstaUsluge vrstaUsluge = new VrstaUsluge().vrstaUslugeNaziv(UPDATED_VRSTA_USLUGE_NAZIV);
        return vrstaUsluge;
    }

    @BeforeEach
    public void initTest() {
        vrstaUsluge = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVrstaUsluge != null) {
            vrstaUslugeRepository.delete(insertedVrstaUsluge);
            insertedVrstaUsluge = null;
        }
    }

    @Test
    @Transactional
    void createVrstaUsluge() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);
        var returnedVrstaUslugeDTO = om.readValue(
            restVrstaUslugeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vrstaUslugeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VrstaUslugeDTO.class
        );

        // Validate the VrstaUsluge in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVrstaUsluge = vrstaUslugeMapper.toEntity(returnedVrstaUslugeDTO);
        assertVrstaUslugeUpdatableFieldsEquals(returnedVrstaUsluge, getPersistedVrstaUsluge(returnedVrstaUsluge));

        insertedVrstaUsluge = returnedVrstaUsluge;
    }

    @Test
    @Transactional
    void createVrstaUslugeWithExistingId() throws Exception {
        // Create the VrstaUsluge with an existing ID
        vrstaUsluge.setId(1L);
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVrstaUslugeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVrstaUslugeNazivIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vrstaUsluge.setVrstaUslugeNaziv(null);

        // Create the VrstaUsluge, which fails.
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        restVrstaUslugeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVrstaUsluges() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vrstaUsluge.getId().intValue())))
            .andExpect(jsonPath("$.[*].vrstaUslugeNaziv").value(hasItem(DEFAULT_VRSTA_USLUGE_NAZIV)));
    }

    @Test
    @Transactional
    void getVrstaUsluge() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get the vrstaUsluge
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL_ID, vrstaUsluge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vrstaUsluge.getId().intValue()))
            .andExpect(jsonPath("$.vrstaUslugeNaziv").value(DEFAULT_VRSTA_USLUGE_NAZIV));
    }

    @Test
    @Transactional
    void getVrstaUslugesByIdFiltering() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        Long id = vrstaUsluge.getId();

        defaultVrstaUslugeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVrstaUslugeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVrstaUslugeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVrstaUslugesByVrstaUslugeNazivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList where vrstaUslugeNaziv equals to
        defaultVrstaUslugeFiltering(
            "vrstaUslugeNaziv.equals=" + DEFAULT_VRSTA_USLUGE_NAZIV,
            "vrstaUslugeNaziv.equals=" + UPDATED_VRSTA_USLUGE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllVrstaUslugesByVrstaUslugeNazivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList where vrstaUslugeNaziv in
        defaultVrstaUslugeFiltering(
            "vrstaUslugeNaziv.in=" + DEFAULT_VRSTA_USLUGE_NAZIV + "," + UPDATED_VRSTA_USLUGE_NAZIV,
            "vrstaUslugeNaziv.in=" + UPDATED_VRSTA_USLUGE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllVrstaUslugesByVrstaUslugeNazivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList where vrstaUslugeNaziv is not null
        defaultVrstaUslugeFiltering("vrstaUslugeNaziv.specified=true", "vrstaUslugeNaziv.specified=false");
    }

    @Test
    @Transactional
    void getAllVrstaUslugesByVrstaUslugeNazivContainsSomething() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList where vrstaUslugeNaziv contains
        defaultVrstaUslugeFiltering(
            "vrstaUslugeNaziv.contains=" + DEFAULT_VRSTA_USLUGE_NAZIV,
            "vrstaUslugeNaziv.contains=" + UPDATED_VRSTA_USLUGE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllVrstaUslugesByVrstaUslugeNazivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        // Get all the vrstaUslugeList where vrstaUslugeNaziv does not contain
        defaultVrstaUslugeFiltering(
            "vrstaUslugeNaziv.doesNotContain=" + UPDATED_VRSTA_USLUGE_NAZIV,
            "vrstaUslugeNaziv.doesNotContain=" + DEFAULT_VRSTA_USLUGE_NAZIV
        );
    }

    private void defaultVrstaUslugeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVrstaUslugeShouldBeFound(shouldBeFound);
        defaultVrstaUslugeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVrstaUslugeShouldBeFound(String filter) throws Exception {
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vrstaUsluge.getId().intValue())))
            .andExpect(jsonPath("$.[*].vrstaUslugeNaziv").value(hasItem(DEFAULT_VRSTA_USLUGE_NAZIV)));

        // Check, that the count call also returns 1
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVrstaUslugeShouldNotBeFound(String filter) throws Exception {
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVrstaUslugeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVrstaUsluge() throws Exception {
        // Get the vrstaUsluge
        restVrstaUslugeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVrstaUsluge() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vrstaUsluge
        VrstaUsluge updatedVrstaUsluge = vrstaUslugeRepository.findById(vrstaUsluge.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVrstaUsluge are not directly saved in db
        em.detach(updatedVrstaUsluge);
        updatedVrstaUsluge.vrstaUslugeNaziv(UPDATED_VRSTA_USLUGE_NAZIV);
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(updatedVrstaUsluge);

        restVrstaUslugeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vrstaUslugeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isOk());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVrstaUslugeToMatchAllProperties(updatedVrstaUsluge);
    }

    @Test
    @Transactional
    void putNonExistingVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vrstaUslugeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vrstaUslugeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVrstaUslugeWithPatch() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vrstaUsluge using partial update
        VrstaUsluge partialUpdatedVrstaUsluge = new VrstaUsluge();
        partialUpdatedVrstaUsluge.setId(vrstaUsluge.getId());

        partialUpdatedVrstaUsluge.vrstaUslugeNaziv(UPDATED_VRSTA_USLUGE_NAZIV);

        restVrstaUslugeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVrstaUsluge.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVrstaUsluge))
            )
            .andExpect(status().isOk());

        // Validate the VrstaUsluge in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVrstaUslugeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVrstaUsluge, vrstaUsluge),
            getPersistedVrstaUsluge(vrstaUsluge)
        );
    }

    @Test
    @Transactional
    void fullUpdateVrstaUslugeWithPatch() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vrstaUsluge using partial update
        VrstaUsluge partialUpdatedVrstaUsluge = new VrstaUsluge();
        partialUpdatedVrstaUsluge.setId(vrstaUsluge.getId());

        partialUpdatedVrstaUsluge.vrstaUslugeNaziv(UPDATED_VRSTA_USLUGE_NAZIV);

        restVrstaUslugeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVrstaUsluge.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVrstaUsluge))
            )
            .andExpect(status().isOk());

        // Validate the VrstaUsluge in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVrstaUslugeUpdatableFieldsEquals(partialUpdatedVrstaUsluge, getPersistedVrstaUsluge(partialUpdatedVrstaUsluge));
    }

    @Test
    @Transactional
    void patchNonExistingVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vrstaUslugeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVrstaUsluge() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vrstaUsluge.setId(longCount.incrementAndGet());

        // Create the VrstaUsluge
        VrstaUslugeDTO vrstaUslugeDTO = vrstaUslugeMapper.toDto(vrstaUsluge);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVrstaUslugeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vrstaUslugeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VrstaUsluge in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVrstaUsluge() throws Exception {
        // Initialize the database
        insertedVrstaUsluge = vrstaUslugeRepository.saveAndFlush(vrstaUsluge);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vrstaUsluge
        restVrstaUslugeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vrstaUsluge.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vrstaUslugeRepository.count();
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

    protected VrstaUsluge getPersistedVrstaUsluge(VrstaUsluge vrstaUsluge) {
        return vrstaUslugeRepository.findById(vrstaUsluge.getId()).orElseThrow();
    }

    protected void assertPersistedVrstaUslugeToMatchAllProperties(VrstaUsluge expectedVrstaUsluge) {
        assertVrstaUslugeAllPropertiesEquals(expectedVrstaUsluge, getPersistedVrstaUsluge(expectedVrstaUsluge));
    }

    protected void assertPersistedVrstaUslugeToMatchUpdatableProperties(VrstaUsluge expectedVrstaUsluge) {
        assertVrstaUslugeAllUpdatablePropertiesEquals(expectedVrstaUsluge, getPersistedVrstaUsluge(expectedVrstaUsluge));
    }
}
