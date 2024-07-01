package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.StatusEvidencijeAsserts.*;
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
import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.repository.StatusEvidencijeRepository;
import org.avd.kamin.service.dto.StatusEvidencijeDTO;
import org.avd.kamin.service.mapper.StatusEvidencijeMapper;
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
 * Integration tests for the {@link StatusEvidencijeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StatusEvidencijeResourceIT {

    private static final String DEFAULT_STATUS_EVIDENCIJE_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_EVIDENCIJE_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/status-evidencijes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private StatusEvidencijeRepository statusEvidencijeRepository;

    @Autowired
    private StatusEvidencijeMapper statusEvidencijeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusEvidencijeMockMvc;

    private StatusEvidencije statusEvidencije;

    private StatusEvidencije insertedStatusEvidencije;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusEvidencije createEntity(EntityManager em) {
        StatusEvidencije statusEvidencije = new StatusEvidencije().statusEvidencijeNaziv(DEFAULT_STATUS_EVIDENCIJE_NAZIV);
        return statusEvidencije;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusEvidencije createUpdatedEntity(EntityManager em) {
        StatusEvidencije statusEvidencije = new StatusEvidencije().statusEvidencijeNaziv(UPDATED_STATUS_EVIDENCIJE_NAZIV);
        return statusEvidencije;
    }

    @BeforeEach
    public void initTest() {
        statusEvidencije = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedStatusEvidencije != null) {
            statusEvidencijeRepository.delete(insertedStatusEvidencije);
            insertedStatusEvidencije = null;
        }
    }

    @Test
    @Transactional
    void createStatusEvidencije() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);
        var returnedStatusEvidencijeDTO = om.readValue(
            restStatusEvidencijeMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(statusEvidencijeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            StatusEvidencijeDTO.class
        );

        // Validate the StatusEvidencije in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedStatusEvidencije = statusEvidencijeMapper.toEntity(returnedStatusEvidencijeDTO);
        assertStatusEvidencijeUpdatableFieldsEquals(returnedStatusEvidencije, getPersistedStatusEvidencije(returnedStatusEvidencije));

        insertedStatusEvidencije = returnedStatusEvidencije;
    }

    @Test
    @Transactional
    void createStatusEvidencijeWithExistingId() throws Exception {
        // Create the StatusEvidencije with an existing ID
        statusEvidencije.setId(1L);
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusEvidencijeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusEvidencijeNazivIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        statusEvidencije.setStatusEvidencijeNaziv(null);

        // Create the StatusEvidencije, which fails.
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        restStatusEvidencijeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStatusEvidencijes() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusEvidencije.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusEvidencijeNaziv").value(hasItem(DEFAULT_STATUS_EVIDENCIJE_NAZIV)));
    }

    @Test
    @Transactional
    void getStatusEvidencije() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get the statusEvidencije
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL_ID, statusEvidencije.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusEvidencije.getId().intValue()))
            .andExpect(jsonPath("$.statusEvidencijeNaziv").value(DEFAULT_STATUS_EVIDENCIJE_NAZIV));
    }

    @Test
    @Transactional
    void getStatusEvidencijesByIdFiltering() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        Long id = statusEvidencije.getId();

        defaultStatusEvidencijeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultStatusEvidencijeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultStatusEvidencijeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStatusEvidencijesByStatusEvidencijeNazivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList where statusEvidencijeNaziv equals to
        defaultStatusEvidencijeFiltering(
            "statusEvidencijeNaziv.equals=" + DEFAULT_STATUS_EVIDENCIJE_NAZIV,
            "statusEvidencijeNaziv.equals=" + UPDATED_STATUS_EVIDENCIJE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllStatusEvidencijesByStatusEvidencijeNazivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList where statusEvidencijeNaziv in
        defaultStatusEvidencijeFiltering(
            "statusEvidencijeNaziv.in=" + DEFAULT_STATUS_EVIDENCIJE_NAZIV + "," + UPDATED_STATUS_EVIDENCIJE_NAZIV,
            "statusEvidencijeNaziv.in=" + UPDATED_STATUS_EVIDENCIJE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllStatusEvidencijesByStatusEvidencijeNazivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList where statusEvidencijeNaziv is not null
        defaultStatusEvidencijeFiltering("statusEvidencijeNaziv.specified=true", "statusEvidencijeNaziv.specified=false");
    }

    @Test
    @Transactional
    void getAllStatusEvidencijesByStatusEvidencijeNazivContainsSomething() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList where statusEvidencijeNaziv contains
        defaultStatusEvidencijeFiltering(
            "statusEvidencijeNaziv.contains=" + DEFAULT_STATUS_EVIDENCIJE_NAZIV,
            "statusEvidencijeNaziv.contains=" + UPDATED_STATUS_EVIDENCIJE_NAZIV
        );
    }

    @Test
    @Transactional
    void getAllStatusEvidencijesByStatusEvidencijeNazivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        // Get all the statusEvidencijeList where statusEvidencijeNaziv does not contain
        defaultStatusEvidencijeFiltering(
            "statusEvidencijeNaziv.doesNotContain=" + UPDATED_STATUS_EVIDENCIJE_NAZIV,
            "statusEvidencijeNaziv.doesNotContain=" + DEFAULT_STATUS_EVIDENCIJE_NAZIV
        );
    }

    private void defaultStatusEvidencijeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultStatusEvidencijeShouldBeFound(shouldBeFound);
        defaultStatusEvidencijeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStatusEvidencijeShouldBeFound(String filter) throws Exception {
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusEvidencije.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusEvidencijeNaziv").value(hasItem(DEFAULT_STATUS_EVIDENCIJE_NAZIV)));

        // Check, that the count call also returns 1
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStatusEvidencijeShouldNotBeFound(String filter) throws Exception {
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStatusEvidencijeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStatusEvidencije() throws Exception {
        // Get the statusEvidencije
        restStatusEvidencijeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStatusEvidencije() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusEvidencije
        StatusEvidencije updatedStatusEvidencije = statusEvidencijeRepository.findById(statusEvidencije.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStatusEvidencije are not directly saved in db
        em.detach(updatedStatusEvidencije);
        updatedStatusEvidencije.statusEvidencijeNaziv(UPDATED_STATUS_EVIDENCIJE_NAZIV);
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(updatedStatusEvidencije);

        restStatusEvidencijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusEvidencijeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isOk());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedStatusEvidencijeToMatchAllProperties(updatedStatusEvidencije);
    }

    @Test
    @Transactional
    void putNonExistingStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, statusEvidencijeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatusEvidencijeWithPatch() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusEvidencije using partial update
        StatusEvidencije partialUpdatedStatusEvidencije = new StatusEvidencije();
        partialUpdatedStatusEvidencije.setId(statusEvidencije.getId());

        partialUpdatedStatusEvidencije.statusEvidencijeNaziv(UPDATED_STATUS_EVIDENCIJE_NAZIV);

        restStatusEvidencijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusEvidencije.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusEvidencije))
            )
            .andExpect(status().isOk());

        // Validate the StatusEvidencije in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusEvidencijeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedStatusEvidencije, statusEvidencije),
            getPersistedStatusEvidencije(statusEvidencije)
        );
    }

    @Test
    @Transactional
    void fullUpdateStatusEvidencijeWithPatch() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the statusEvidencije using partial update
        StatusEvidencije partialUpdatedStatusEvidencije = new StatusEvidencije();
        partialUpdatedStatusEvidencije.setId(statusEvidencije.getId());

        partialUpdatedStatusEvidencije.statusEvidencijeNaziv(UPDATED_STATUS_EVIDENCIJE_NAZIV);

        restStatusEvidencijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStatusEvidencije.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedStatusEvidencije))
            )
            .andExpect(status().isOk());

        // Validate the StatusEvidencije in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertStatusEvidencijeUpdatableFieldsEquals(
            partialUpdatedStatusEvidencije,
            getPersistedStatusEvidencije(partialUpdatedStatusEvidencije)
        );
    }

    @Test
    @Transactional
    void patchNonExistingStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, statusEvidencijeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStatusEvidencije() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        statusEvidencije.setId(longCount.incrementAndGet());

        // Create the StatusEvidencije
        StatusEvidencijeDTO statusEvidencijeDTO = statusEvidencijeMapper.toDto(statusEvidencije);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatusEvidencijeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(statusEvidencijeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StatusEvidencije in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStatusEvidencije() throws Exception {
        // Initialize the database
        insertedStatusEvidencije = statusEvidencijeRepository.saveAndFlush(statusEvidencije);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the statusEvidencije
        restStatusEvidencijeMockMvc
            .perform(delete(ENTITY_API_URL_ID, statusEvidencije.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return statusEvidencijeRepository.count();
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

    protected StatusEvidencije getPersistedStatusEvidencije(StatusEvidencije statusEvidencije) {
        return statusEvidencijeRepository.findById(statusEvidencije.getId()).orElseThrow();
    }

    protected void assertPersistedStatusEvidencijeToMatchAllProperties(StatusEvidencije expectedStatusEvidencije) {
        assertStatusEvidencijeAllPropertiesEquals(expectedStatusEvidencije, getPersistedStatusEvidencije(expectedStatusEvidencije));
    }

    protected void assertPersistedStatusEvidencijeToMatchUpdatableProperties(StatusEvidencije expectedStatusEvidencije) {
        assertStatusEvidencijeAllUpdatablePropertiesEquals(
            expectedStatusEvidencije,
            getPersistedStatusEvidencije(expectedStatusEvidencije)
        );
    }
}
