package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.GradAsserts.*;
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
import org.avd.kamin.domain.Grad;
import org.avd.kamin.repository.GradRepository;
import org.avd.kamin.service.dto.GradDTO;
import org.avd.kamin.service.mapper.GradMapper;
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
 * Integration tests for the {@link GradResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GradResourceIT {

    private static final String DEFAULT_GRAD_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_GRAD_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GradRepository gradRepository;

    @Autowired
    private GradMapper gradMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGradMockMvc;

    private Grad grad;

    private Grad insertedGrad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grad createEntity(EntityManager em) {
        Grad grad = new Grad().gradNaziv(DEFAULT_GRAD_NAZIV);
        return grad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grad createUpdatedEntity(EntityManager em) {
        Grad grad = new Grad().gradNaziv(UPDATED_GRAD_NAZIV);
        return grad;
    }

    @BeforeEach
    public void initTest() {
        grad = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGrad != null) {
            gradRepository.delete(insertedGrad);
            insertedGrad = null;
        }
    }

    @Test
    @Transactional
    void createGrad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);
        var returnedGradDTO = om.readValue(
            restGradMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gradDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GradDTO.class
        );

        // Validate the Grad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGrad = gradMapper.toEntity(returnedGradDTO);
        assertGradUpdatableFieldsEquals(returnedGrad, getPersistedGrad(returnedGrad));

        insertedGrad = returnedGrad;
    }

    @Test
    @Transactional
    void createGradWithExistingId() throws Exception {
        // Create the Grad with an existing ID
        grad.setId(1L);
        GradDTO gradDTO = gradMapper.toDto(grad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGradMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gradDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGradNazivIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        grad.setGradNaziv(null);

        // Create the Grad, which fails.
        GradDTO gradDTO = gradMapper.toDto(grad);

        restGradMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gradDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrads() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grad.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradNaziv").value(hasItem(DEFAULT_GRAD_NAZIV)));
    }

    @Test
    @Transactional
    void getGrad() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get the grad
        restGradMockMvc
            .perform(get(ENTITY_API_URL_ID, grad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grad.getId().intValue()))
            .andExpect(jsonPath("$.gradNaziv").value(DEFAULT_GRAD_NAZIV));
    }

    @Test
    @Transactional
    void getGradsByIdFiltering() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        Long id = grad.getId();

        defaultGradFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultGradFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultGradFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGradsByGradNazivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList where gradNaziv equals to
        defaultGradFiltering("gradNaziv.equals=" + DEFAULT_GRAD_NAZIV, "gradNaziv.equals=" + UPDATED_GRAD_NAZIV);
    }

    @Test
    @Transactional
    void getAllGradsByGradNazivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList where gradNaziv in
        defaultGradFiltering("gradNaziv.in=" + DEFAULT_GRAD_NAZIV + "," + UPDATED_GRAD_NAZIV, "gradNaziv.in=" + UPDATED_GRAD_NAZIV);
    }

    @Test
    @Transactional
    void getAllGradsByGradNazivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList where gradNaziv is not null
        defaultGradFiltering("gradNaziv.specified=true", "gradNaziv.specified=false");
    }

    @Test
    @Transactional
    void getAllGradsByGradNazivContainsSomething() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList where gradNaziv contains
        defaultGradFiltering("gradNaziv.contains=" + DEFAULT_GRAD_NAZIV, "gradNaziv.contains=" + UPDATED_GRAD_NAZIV);
    }

    @Test
    @Transactional
    void getAllGradsByGradNazivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        // Get all the gradList where gradNaziv does not contain
        defaultGradFiltering("gradNaziv.doesNotContain=" + UPDATED_GRAD_NAZIV, "gradNaziv.doesNotContain=" + DEFAULT_GRAD_NAZIV);
    }

    private void defaultGradFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGradShouldBeFound(shouldBeFound);
        defaultGradShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGradShouldBeFound(String filter) throws Exception {
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grad.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradNaziv").value(hasItem(DEFAULT_GRAD_NAZIV)));

        // Check, that the count call also returns 1
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGradShouldNotBeFound(String filter) throws Exception {
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGrad() throws Exception {
        // Get the grad
        restGradMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGrad() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grad
        Grad updatedGrad = gradRepository.findById(grad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGrad are not directly saved in db
        em.detach(updatedGrad);
        updatedGrad.gradNaziv(UPDATED_GRAD_NAZIV);
        GradDTO gradDTO = gradMapper.toDto(updatedGrad);

        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gradDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gradDTO))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGradToMatchAllProperties(updatedGrad);
    }

    @Test
    @Transactional
    void putNonExistingGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gradDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gradDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(gradDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(gradDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGradWithPatch() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grad using partial update
        Grad partialUpdatedGrad = new Grad();
        partialUpdatedGrad.setId(grad.getId());

        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrad))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGradUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGrad, grad), getPersistedGrad(grad));
    }

    @Test
    @Transactional
    void fullUpdateGradWithPatch() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the grad using partial update
        Grad partialUpdatedGrad = new Grad();
        partialUpdatedGrad.setId(grad.getId());

        partialUpdatedGrad.gradNaziv(UPDATED_GRAD_NAZIV);

        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrad.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGrad))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGradUpdatableFieldsEquals(partialUpdatedGrad, getPersistedGrad(partialUpdatedGrad));
    }

    @Test
    @Transactional
    void patchNonExistingGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gradDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gradDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(gradDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        grad.setId(longCount.incrementAndGet());

        // Create the Grad
        GradDTO gradDTO = gradMapper.toDto(grad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(gradDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrad() throws Exception {
        // Initialize the database
        insertedGrad = gradRepository.saveAndFlush(grad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the grad
        restGradMockMvc
            .perform(delete(ENTITY_API_URL_ID, grad.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gradRepository.count();
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

    protected Grad getPersistedGrad(Grad grad) {
        return gradRepository.findById(grad.getId()).orElseThrow();
    }

    protected void assertPersistedGradToMatchAllProperties(Grad expectedGrad) {
        assertGradAllPropertiesEquals(expectedGrad, getPersistedGrad(expectedGrad));
    }

    protected void assertPersistedGradToMatchUpdatableProperties(Grad expectedGrad) {
        assertGradAllUpdatablePropertiesEquals(expectedGrad, getPersistedGrad(expectedGrad));
    }
}
