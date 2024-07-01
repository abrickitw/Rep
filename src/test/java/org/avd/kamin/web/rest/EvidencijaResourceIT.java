package org.avd.kamin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.avd.kamin.domain.EvidencijaAsserts.*;
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
import org.avd.kamin.domain.Evidencija;
import org.avd.kamin.domain.Raspored;
import org.avd.kamin.domain.StatusEvidencije;
import org.avd.kamin.domain.User;
import org.avd.kamin.domain.VrstaUsluge;
import org.avd.kamin.repository.EvidencijaRepository;
import org.avd.kamin.repository.UserRepository;
import org.avd.kamin.service.EvidencijaService;
import org.avd.kamin.service.dto.EvidencijaDTO;
import org.avd.kamin.service.mapper.EvidencijaMapper;
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
 * Integration tests for the {@link EvidencijaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvidencijaResourceIT {

    private static final String DEFAULT_NAZIV_EVIDENCIJA = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV_EVIDENCIJA = "BBBBBBBBBB";

    private static final String DEFAULT_VRIJEME_USLUGE = "AAAAAAAAAA";
    private static final String UPDATED_VRIJEME_USLUGE = "BBBBBBBBBB";

    private static final String DEFAULT_KOMENTAR = "AAAAAAAAAA";
    private static final String UPDATED_KOMENTAR = "BBBBBBBBBB";

    private static final String DEFAULT_IME_STANARA = "AAAAAAAAAA";
    private static final String UPDATED_IME_STANARA = "BBBBBBBBBB";

    private static final String DEFAULT_PREZIME_STANARA = "AAAAAAAAAA";
    private static final String UPDATED_PREZIME_STANARA = "BBBBBBBBBB";

    private static final String DEFAULT_KONTAKT_STANARA = "AAAAAAAAAA";
    private static final String UPDATED_KONTAKT_STANARA = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM_ISPRAVKA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_ISPRAVKA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATUM_ISPRAVKA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_KOMENTAR_ISPRAVKA = "AAAAAAAAAA";
    private static final String UPDATED_KOMENTAR_ISPRAVKA = "BBBBBBBBBB";

    private static final String DEFAULT_KUCNI_BROJ = "AAAAAAAAAA";
    private static final String UPDATED_KUCNI_BROJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evidencijas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EvidencijaRepository evidencijaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EvidencijaRepository evidencijaRepositoryMock;

    @Autowired
    private EvidencijaMapper evidencijaMapper;

    @Mock
    private EvidencijaService evidencijaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvidencijaMockMvc;

    private Evidencija evidencija;

    private Evidencija insertedEvidencija;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evidencija createEntity(EntityManager em) {
        Evidencija evidencija = new Evidencija()
            .nazivEvidencija(DEFAULT_NAZIV_EVIDENCIJA)
            .vrijemeUsluge(DEFAULT_VRIJEME_USLUGE)
            .komentar(DEFAULT_KOMENTAR)
            .imeStanara(DEFAULT_IME_STANARA)
            .prezimeStanara(DEFAULT_PREZIME_STANARA)
            .kontaktStanara(DEFAULT_KONTAKT_STANARA)
            .datumIspravka(DEFAULT_DATUM_ISPRAVKA)
            .komentarIspravka(DEFAULT_KOMENTAR_ISPRAVKA)
            .kucniBroj(DEFAULT_KUCNI_BROJ);
        return evidencija;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evidencija createUpdatedEntity(EntityManager em) {
        Evidencija evidencija = new Evidencija()
            .nazivEvidencija(UPDATED_NAZIV_EVIDENCIJA)
            .vrijemeUsluge(UPDATED_VRIJEME_USLUGE)
            .komentar(UPDATED_KOMENTAR)
            .imeStanara(UPDATED_IME_STANARA)
            .prezimeStanara(UPDATED_PREZIME_STANARA)
            .kontaktStanara(UPDATED_KONTAKT_STANARA)
            .datumIspravka(UPDATED_DATUM_ISPRAVKA)
            .komentarIspravka(UPDATED_KOMENTAR_ISPRAVKA)
            .kucniBroj(UPDATED_KUCNI_BROJ);
        return evidencija;
    }

    @BeforeEach
    public void initTest() {
        evidencija = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedEvidencija != null) {
            evidencijaRepository.delete(insertedEvidencija);
            insertedEvidencija = null;
        }
    }

    @Test
    @Transactional
    void createEvidencija() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);
        var returnedEvidencijaDTO = om.readValue(
            restEvidencijaMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EvidencijaDTO.class
        );

        // Validate the Evidencija in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEvidencija = evidencijaMapper.toEntity(returnedEvidencijaDTO);
        assertEvidencijaUpdatableFieldsEquals(returnedEvidencija, getPersistedEvidencija(returnedEvidencija));

        insertedEvidencija = returnedEvidencija;
    }

    @Test
    @Transactional
    void createEvidencijaWithExistingId() throws Exception {
        // Create the Evidencija with an existing ID
        evidencija.setId(1L);
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNazivEvidencijaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setNazivEvidencija(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVrijemeUslugeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setVrijemeUsluge(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKomentarIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setKomentar(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImeStanaraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setImeStanara(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrezimeStanaraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setPrezimeStanara(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKontaktStanaraIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        evidencija.setKontaktStanara(null);

        // Create the Evidencija, which fails.
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        restEvidencijaMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvidencijas() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evidencija.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazivEvidencija").value(hasItem(DEFAULT_NAZIV_EVIDENCIJA)))
            .andExpect(jsonPath("$.[*].vrijemeUsluge").value(hasItem(DEFAULT_VRIJEME_USLUGE)))
            .andExpect(jsonPath("$.[*].komentar").value(hasItem(DEFAULT_KOMENTAR)))
            .andExpect(jsonPath("$.[*].imeStanara").value(hasItem(DEFAULT_IME_STANARA)))
            .andExpect(jsonPath("$.[*].prezimeStanara").value(hasItem(DEFAULT_PREZIME_STANARA)))
            .andExpect(jsonPath("$.[*].kontaktStanara").value(hasItem(DEFAULT_KONTAKT_STANARA)))
            .andExpect(jsonPath("$.[*].datumIspravka").value(hasItem(DEFAULT_DATUM_ISPRAVKA.toString())))
            .andExpect(jsonPath("$.[*].komentarIspravka").value(hasItem(DEFAULT_KOMENTAR_ISPRAVKA)))
            .andExpect(jsonPath("$.[*].kucniBroj").value(hasItem(DEFAULT_KUCNI_BROJ)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvidencijasWithEagerRelationshipsIsEnabled() throws Exception {
        when(evidencijaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvidencijaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evidencijaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvidencijasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evidencijaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvidencijaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evidencijaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvidencija() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get the evidencija
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL_ID, evidencija.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evidencija.getId().intValue()))
            .andExpect(jsonPath("$.nazivEvidencija").value(DEFAULT_NAZIV_EVIDENCIJA))
            .andExpect(jsonPath("$.vrijemeUsluge").value(DEFAULT_VRIJEME_USLUGE))
            .andExpect(jsonPath("$.komentar").value(DEFAULT_KOMENTAR))
            .andExpect(jsonPath("$.imeStanara").value(DEFAULT_IME_STANARA))
            .andExpect(jsonPath("$.prezimeStanara").value(DEFAULT_PREZIME_STANARA))
            .andExpect(jsonPath("$.kontaktStanara").value(DEFAULT_KONTAKT_STANARA))
            .andExpect(jsonPath("$.datumIspravka").value(DEFAULT_DATUM_ISPRAVKA.toString()))
            .andExpect(jsonPath("$.komentarIspravka").value(DEFAULT_KOMENTAR_ISPRAVKA))
            .andExpect(jsonPath("$.kucniBroj").value(DEFAULT_KUCNI_BROJ));
    }

    @Test
    @Transactional
    void getEvidencijasByIdFiltering() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        Long id = evidencija.getId();

        defaultEvidencijaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEvidencijaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEvidencijaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvidencijasByNazivEvidencijaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where nazivEvidencija equals to
        defaultEvidencijaFiltering(
            "nazivEvidencija.equals=" + DEFAULT_NAZIV_EVIDENCIJA,
            "nazivEvidencija.equals=" + UPDATED_NAZIV_EVIDENCIJA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByNazivEvidencijaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where nazivEvidencija in
        defaultEvidencijaFiltering(
            "nazivEvidencija.in=" + DEFAULT_NAZIV_EVIDENCIJA + "," + UPDATED_NAZIV_EVIDENCIJA,
            "nazivEvidencija.in=" + UPDATED_NAZIV_EVIDENCIJA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByNazivEvidencijaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where nazivEvidencija is not null
        defaultEvidencijaFiltering("nazivEvidencija.specified=true", "nazivEvidencija.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByNazivEvidencijaContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where nazivEvidencija contains
        defaultEvidencijaFiltering(
            "nazivEvidencija.contains=" + DEFAULT_NAZIV_EVIDENCIJA,
            "nazivEvidencija.contains=" + UPDATED_NAZIV_EVIDENCIJA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByNazivEvidencijaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where nazivEvidencija does not contain
        defaultEvidencijaFiltering(
            "nazivEvidencija.doesNotContain=" + UPDATED_NAZIV_EVIDENCIJA,
            "nazivEvidencija.doesNotContain=" + DEFAULT_NAZIV_EVIDENCIJA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrijemeUslugeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where vrijemeUsluge equals to
        defaultEvidencijaFiltering("vrijemeUsluge.equals=" + DEFAULT_VRIJEME_USLUGE, "vrijemeUsluge.equals=" + UPDATED_VRIJEME_USLUGE);
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrijemeUslugeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where vrijemeUsluge in
        defaultEvidencijaFiltering(
            "vrijemeUsluge.in=" + DEFAULT_VRIJEME_USLUGE + "," + UPDATED_VRIJEME_USLUGE,
            "vrijemeUsluge.in=" + UPDATED_VRIJEME_USLUGE
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrijemeUslugeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where vrijemeUsluge is not null
        defaultEvidencijaFiltering("vrijemeUsluge.specified=true", "vrijemeUsluge.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrijemeUslugeContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where vrijemeUsluge contains
        defaultEvidencijaFiltering("vrijemeUsluge.contains=" + DEFAULT_VRIJEME_USLUGE, "vrijemeUsluge.contains=" + UPDATED_VRIJEME_USLUGE);
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrijemeUslugeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where vrijemeUsluge does not contain
        defaultEvidencijaFiltering(
            "vrijemeUsluge.doesNotContain=" + UPDATED_VRIJEME_USLUGE,
            "vrijemeUsluge.doesNotContain=" + DEFAULT_VRIJEME_USLUGE
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentar equals to
        defaultEvidencijaFiltering("komentar.equals=" + DEFAULT_KOMENTAR, "komentar.equals=" + UPDATED_KOMENTAR);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentar in
        defaultEvidencijaFiltering("komentar.in=" + DEFAULT_KOMENTAR + "," + UPDATED_KOMENTAR, "komentar.in=" + UPDATED_KOMENTAR);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentar is not null
        defaultEvidencijaFiltering("komentar.specified=true", "komentar.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentar contains
        defaultEvidencijaFiltering("komentar.contains=" + DEFAULT_KOMENTAR, "komentar.contains=" + UPDATED_KOMENTAR);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentar does not contain
        defaultEvidencijaFiltering("komentar.doesNotContain=" + UPDATED_KOMENTAR, "komentar.doesNotContain=" + DEFAULT_KOMENTAR);
    }

    @Test
    @Transactional
    void getAllEvidencijasByImeStanaraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where imeStanara equals to
        defaultEvidencijaFiltering("imeStanara.equals=" + DEFAULT_IME_STANARA, "imeStanara.equals=" + UPDATED_IME_STANARA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByImeStanaraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where imeStanara in
        defaultEvidencijaFiltering(
            "imeStanara.in=" + DEFAULT_IME_STANARA + "," + UPDATED_IME_STANARA,
            "imeStanara.in=" + UPDATED_IME_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByImeStanaraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where imeStanara is not null
        defaultEvidencijaFiltering("imeStanara.specified=true", "imeStanara.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByImeStanaraContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where imeStanara contains
        defaultEvidencijaFiltering("imeStanara.contains=" + DEFAULT_IME_STANARA, "imeStanara.contains=" + UPDATED_IME_STANARA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByImeStanaraNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where imeStanara does not contain
        defaultEvidencijaFiltering("imeStanara.doesNotContain=" + UPDATED_IME_STANARA, "imeStanara.doesNotContain=" + DEFAULT_IME_STANARA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByPrezimeStanaraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where prezimeStanara equals to
        defaultEvidencijaFiltering("prezimeStanara.equals=" + DEFAULT_PREZIME_STANARA, "prezimeStanara.equals=" + UPDATED_PREZIME_STANARA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByPrezimeStanaraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where prezimeStanara in
        defaultEvidencijaFiltering(
            "prezimeStanara.in=" + DEFAULT_PREZIME_STANARA + "," + UPDATED_PREZIME_STANARA,
            "prezimeStanara.in=" + UPDATED_PREZIME_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByPrezimeStanaraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where prezimeStanara is not null
        defaultEvidencijaFiltering("prezimeStanara.specified=true", "prezimeStanara.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByPrezimeStanaraContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where prezimeStanara contains
        defaultEvidencijaFiltering(
            "prezimeStanara.contains=" + DEFAULT_PREZIME_STANARA,
            "prezimeStanara.contains=" + UPDATED_PREZIME_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByPrezimeStanaraNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where prezimeStanara does not contain
        defaultEvidencijaFiltering(
            "prezimeStanara.doesNotContain=" + UPDATED_PREZIME_STANARA,
            "prezimeStanara.doesNotContain=" + DEFAULT_PREZIME_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKontaktStanaraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kontaktStanara equals to
        defaultEvidencijaFiltering("kontaktStanara.equals=" + DEFAULT_KONTAKT_STANARA, "kontaktStanara.equals=" + UPDATED_KONTAKT_STANARA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKontaktStanaraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kontaktStanara in
        defaultEvidencijaFiltering(
            "kontaktStanara.in=" + DEFAULT_KONTAKT_STANARA + "," + UPDATED_KONTAKT_STANARA,
            "kontaktStanara.in=" + UPDATED_KONTAKT_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKontaktStanaraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kontaktStanara is not null
        defaultEvidencijaFiltering("kontaktStanara.specified=true", "kontaktStanara.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByKontaktStanaraContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kontaktStanara contains
        defaultEvidencijaFiltering(
            "kontaktStanara.contains=" + DEFAULT_KONTAKT_STANARA,
            "kontaktStanara.contains=" + UPDATED_KONTAKT_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKontaktStanaraNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kontaktStanara does not contain
        defaultEvidencijaFiltering(
            "kontaktStanara.doesNotContain=" + UPDATED_KONTAKT_STANARA,
            "kontaktStanara.doesNotContain=" + DEFAULT_KONTAKT_STANARA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka equals to
        defaultEvidencijaFiltering("datumIspravka.equals=" + DEFAULT_DATUM_ISPRAVKA, "datumIspravka.equals=" + UPDATED_DATUM_ISPRAVKA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka in
        defaultEvidencijaFiltering(
            "datumIspravka.in=" + DEFAULT_DATUM_ISPRAVKA + "," + UPDATED_DATUM_ISPRAVKA,
            "datumIspravka.in=" + UPDATED_DATUM_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka is not null
        defaultEvidencijaFiltering("datumIspravka.specified=true", "datumIspravka.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka is greater than or equal to
        defaultEvidencijaFiltering(
            "datumIspravka.greaterThanOrEqual=" + DEFAULT_DATUM_ISPRAVKA,
            "datumIspravka.greaterThanOrEqual=" + UPDATED_DATUM_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka is less than or equal to
        defaultEvidencijaFiltering(
            "datumIspravka.lessThanOrEqual=" + DEFAULT_DATUM_ISPRAVKA,
            "datumIspravka.lessThanOrEqual=" + SMALLER_DATUM_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka is less than
        defaultEvidencijaFiltering("datumIspravka.lessThan=" + UPDATED_DATUM_ISPRAVKA, "datumIspravka.lessThan=" + DEFAULT_DATUM_ISPRAVKA);
    }

    @Test
    @Transactional
    void getAllEvidencijasByDatumIspravkaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where datumIspravka is greater than
        defaultEvidencijaFiltering(
            "datumIspravka.greaterThan=" + SMALLER_DATUM_ISPRAVKA,
            "datumIspravka.greaterThan=" + DEFAULT_DATUM_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIspravkaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentarIspravka equals to
        defaultEvidencijaFiltering(
            "komentarIspravka.equals=" + DEFAULT_KOMENTAR_ISPRAVKA,
            "komentarIspravka.equals=" + UPDATED_KOMENTAR_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIspravkaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentarIspravka in
        defaultEvidencijaFiltering(
            "komentarIspravka.in=" + DEFAULT_KOMENTAR_ISPRAVKA + "," + UPDATED_KOMENTAR_ISPRAVKA,
            "komentarIspravka.in=" + UPDATED_KOMENTAR_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIspravkaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentarIspravka is not null
        defaultEvidencijaFiltering("komentarIspravka.specified=true", "komentarIspravka.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIspravkaContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentarIspravka contains
        defaultEvidencijaFiltering(
            "komentarIspravka.contains=" + DEFAULT_KOMENTAR_ISPRAVKA,
            "komentarIspravka.contains=" + UPDATED_KOMENTAR_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKomentarIspravkaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where komentarIspravka does not contain
        defaultEvidencijaFiltering(
            "komentarIspravka.doesNotContain=" + UPDATED_KOMENTAR_ISPRAVKA,
            "komentarIspravka.doesNotContain=" + DEFAULT_KOMENTAR_ISPRAVKA
        );
    }

    @Test
    @Transactional
    void getAllEvidencijasByKucniBrojIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kucniBroj equals to
        defaultEvidencijaFiltering("kucniBroj.equals=" + DEFAULT_KUCNI_BROJ, "kucniBroj.equals=" + UPDATED_KUCNI_BROJ);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKucniBrojIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kucniBroj in
        defaultEvidencijaFiltering("kucniBroj.in=" + DEFAULT_KUCNI_BROJ + "," + UPDATED_KUCNI_BROJ, "kucniBroj.in=" + UPDATED_KUCNI_BROJ);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKucniBrojIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kucniBroj is not null
        defaultEvidencijaFiltering("kucniBroj.specified=true", "kucniBroj.specified=false");
    }

    @Test
    @Transactional
    void getAllEvidencijasByKucniBrojContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kucniBroj contains
        defaultEvidencijaFiltering("kucniBroj.contains=" + DEFAULT_KUCNI_BROJ, "kucniBroj.contains=" + UPDATED_KUCNI_BROJ);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKucniBrojNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        // Get all the evidencijaList where kucniBroj does not contain
        defaultEvidencijaFiltering("kucniBroj.doesNotContain=" + UPDATED_KUCNI_BROJ, "kucniBroj.doesNotContain=" + DEFAULT_KUCNI_BROJ);
    }

    @Test
    @Transactional
    void getAllEvidencijasByKorisnikIzvrsioIsEqualToSomething() throws Exception {
        User korisnikIzvrsio;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            evidencijaRepository.saveAndFlush(evidencija);
            korisnikIzvrsio = UserResourceIT.createEntity(em);
        } else {
            korisnikIzvrsio = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(korisnikIzvrsio);
        em.flush();
        evidencija.setKorisnikIzvrsio(korisnikIzvrsio);
        evidencijaRepository.saveAndFlush(evidencija);
        Long korisnikIzvrsioId = korisnikIzvrsio.getId();
        // Get all the evidencijaList where korisnikIzvrsio equals to korisnikIzvrsioId
        defaultEvidencijaShouldBeFound("korisnikIzvrsioId.equals=" + korisnikIzvrsioId);

        // Get all the evidencijaList where korisnikIzvrsio equals to (korisnikIzvrsioId + 1)
        defaultEvidencijaShouldNotBeFound("korisnikIzvrsioId.equals=" + (korisnikIzvrsioId + 1));
    }

    @Test
    @Transactional
    void getAllEvidencijasByKorisnikIspravioIsEqualToSomething() throws Exception {
        User korisnikIspravio;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            evidencijaRepository.saveAndFlush(evidencija);
            korisnikIspravio = UserResourceIT.createEntity(em);
        } else {
            korisnikIspravio = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(korisnikIspravio);
        em.flush();
        evidencija.setKorisnikIspravio(korisnikIspravio);
        evidencijaRepository.saveAndFlush(evidencija);
        Long korisnikIspravioId = korisnikIspravio.getId();
        // Get all the evidencijaList where korisnikIspravio equals to korisnikIspravioId
        defaultEvidencijaShouldBeFound("korisnikIspravioId.equals=" + korisnikIspravioId);

        // Get all the evidencijaList where korisnikIspravio equals to (korisnikIspravioId + 1)
        defaultEvidencijaShouldNotBeFound("korisnikIspravioId.equals=" + (korisnikIspravioId + 1));
    }

    @Test
    @Transactional
    void getAllEvidencijasByRasporedIsEqualToSomething() throws Exception {
        Raspored raspored;
        if (TestUtil.findAll(em, Raspored.class).isEmpty()) {
            evidencijaRepository.saveAndFlush(evidencija);
            raspored = RasporedResourceIT.createEntity(em);
        } else {
            raspored = TestUtil.findAll(em, Raspored.class).get(0);
        }
        em.persist(raspored);
        em.flush();
        evidencija.setRaspored(raspored);
        evidencijaRepository.saveAndFlush(evidencija);
        Long rasporedId = raspored.getId();
        // Get all the evidencijaList where raspored equals to rasporedId
        defaultEvidencijaShouldBeFound("rasporedId.equals=" + rasporedId);

        // Get all the evidencijaList where raspored equals to (rasporedId + 1)
        defaultEvidencijaShouldNotBeFound("rasporedId.equals=" + (rasporedId + 1));
    }

    @Test
    @Transactional
    void getAllEvidencijasByVrstaUslugeIsEqualToSomething() throws Exception {
        VrstaUsluge vrstaUsluge;
        if (TestUtil.findAll(em, VrstaUsluge.class).isEmpty()) {
            evidencijaRepository.saveAndFlush(evidencija);
            vrstaUsluge = VrstaUslugeResourceIT.createEntity(em);
        } else {
            vrstaUsluge = TestUtil.findAll(em, VrstaUsluge.class).get(0);
        }
        em.persist(vrstaUsluge);
        em.flush();
        evidencija.setVrstaUsluge(vrstaUsluge);
        evidencijaRepository.saveAndFlush(evidencija);
        Long vrstaUslugeId = vrstaUsluge.getId();
        // Get all the evidencijaList where vrstaUsluge equals to vrstaUslugeId
        defaultEvidencijaShouldBeFound("vrstaUslugeId.equals=" + vrstaUslugeId);

        // Get all the evidencijaList where vrstaUsluge equals to (vrstaUslugeId + 1)
        defaultEvidencijaShouldNotBeFound("vrstaUslugeId.equals=" + (vrstaUslugeId + 1));
    }

    @Test
    @Transactional
    void getAllEvidencijasByStatusEvidencijeIsEqualToSomething() throws Exception {
        StatusEvidencije statusEvidencije;
        if (TestUtil.findAll(em, StatusEvidencije.class).isEmpty()) {
            evidencijaRepository.saveAndFlush(evidencija);
            statusEvidencije = StatusEvidencijeResourceIT.createEntity(em);
        } else {
            statusEvidencije = TestUtil.findAll(em, StatusEvidencije.class).get(0);
        }
        em.persist(statusEvidencije);
        em.flush();
        evidencija.setStatusEvidencije(statusEvidencije);
        evidencijaRepository.saveAndFlush(evidencija);
        Long statusEvidencijeId = statusEvidencije.getId();
        // Get all the evidencijaList where statusEvidencije equals to statusEvidencijeId
        defaultEvidencijaShouldBeFound("statusEvidencijeId.equals=" + statusEvidencijeId);

        // Get all the evidencijaList where statusEvidencije equals to (statusEvidencijeId + 1)
        defaultEvidencijaShouldNotBeFound("statusEvidencijeId.equals=" + (statusEvidencijeId + 1));
    }

    private void defaultEvidencijaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEvidencijaShouldBeFound(shouldBeFound);
        defaultEvidencijaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvidencijaShouldBeFound(String filter) throws Exception {
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evidencija.getId().intValue())))
            .andExpect(jsonPath("$.[*].nazivEvidencija").value(hasItem(DEFAULT_NAZIV_EVIDENCIJA)))
            .andExpect(jsonPath("$.[*].vrijemeUsluge").value(hasItem(DEFAULT_VRIJEME_USLUGE)))
            .andExpect(jsonPath("$.[*].komentar").value(hasItem(DEFAULT_KOMENTAR)))
            .andExpect(jsonPath("$.[*].imeStanara").value(hasItem(DEFAULT_IME_STANARA)))
            .andExpect(jsonPath("$.[*].prezimeStanara").value(hasItem(DEFAULT_PREZIME_STANARA)))
            .andExpect(jsonPath("$.[*].kontaktStanara").value(hasItem(DEFAULT_KONTAKT_STANARA)))
            .andExpect(jsonPath("$.[*].datumIspravka").value(hasItem(DEFAULT_DATUM_ISPRAVKA.toString())))
            .andExpect(jsonPath("$.[*].komentarIspravka").value(hasItem(DEFAULT_KOMENTAR_ISPRAVKA)))
            .andExpect(jsonPath("$.[*].kucniBroj").value(hasItem(DEFAULT_KUCNI_BROJ)));

        // Check, that the count call also returns 1
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvidencijaShouldNotBeFound(String filter) throws Exception {
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvidencijaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvidencija() throws Exception {
        // Get the evidencija
        restEvidencijaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvidencija() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evidencija
        Evidencija updatedEvidencija = evidencijaRepository.findById(evidencija.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvidencija are not directly saved in db
        em.detach(updatedEvidencija);
        updatedEvidencija
            .nazivEvidencija(UPDATED_NAZIV_EVIDENCIJA)
            .vrijemeUsluge(UPDATED_VRIJEME_USLUGE)
            .komentar(UPDATED_KOMENTAR)
            .imeStanara(UPDATED_IME_STANARA)
            .prezimeStanara(UPDATED_PREZIME_STANARA)
            .kontaktStanara(UPDATED_KONTAKT_STANARA)
            .datumIspravka(UPDATED_DATUM_ISPRAVKA)
            .komentarIspravka(UPDATED_KOMENTAR_ISPRAVKA)
            .kucniBroj(UPDATED_KUCNI_BROJ);
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(updatedEvidencija);

        restEvidencijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evidencijaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEvidencijaToMatchAllProperties(updatedEvidencija);
    }

    @Test
    @Transactional
    void putNonExistingEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evidencijaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(evidencijaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvidencijaWithPatch() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evidencija using partial update
        Evidencija partialUpdatedEvidencija = new Evidencija();
        partialUpdatedEvidencija.setId(evidencija.getId());

        partialUpdatedEvidencija
            .nazivEvidencija(UPDATED_NAZIV_EVIDENCIJA)
            .datumIspravka(UPDATED_DATUM_ISPRAVKA)
            .komentarIspravka(UPDATED_KOMENTAR_ISPRAVKA)
            .kucniBroj(UPDATED_KUCNI_BROJ);

        restEvidencijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvidencija.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvidencija))
            )
            .andExpect(status().isOk());

        // Validate the Evidencija in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvidencijaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEvidencija, evidencija),
            getPersistedEvidencija(evidencija)
        );
    }

    @Test
    @Transactional
    void fullUpdateEvidencijaWithPatch() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the evidencija using partial update
        Evidencija partialUpdatedEvidencija = new Evidencija();
        partialUpdatedEvidencija.setId(evidencija.getId());

        partialUpdatedEvidencija
            .nazivEvidencija(UPDATED_NAZIV_EVIDENCIJA)
            .vrijemeUsluge(UPDATED_VRIJEME_USLUGE)
            .komentar(UPDATED_KOMENTAR)
            .imeStanara(UPDATED_IME_STANARA)
            .prezimeStanara(UPDATED_PREZIME_STANARA)
            .kontaktStanara(UPDATED_KONTAKT_STANARA)
            .datumIspravka(UPDATED_DATUM_ISPRAVKA)
            .komentarIspravka(UPDATED_KOMENTAR_ISPRAVKA)
            .kucniBroj(UPDATED_KUCNI_BROJ);

        restEvidencijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvidencija.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvidencija))
            )
            .andExpect(status().isOk());

        // Validate the Evidencija in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEvidencijaUpdatableFieldsEquals(partialUpdatedEvidencija, getPersistedEvidencija(partialUpdatedEvidencija));
    }

    @Test
    @Transactional
    void patchNonExistingEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evidencijaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvidencija() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        evidencija.setId(longCount.incrementAndGet());

        // Create the Evidencija
        EvidencijaDTO evidencijaDTO = evidencijaMapper.toDto(evidencija);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvidencijaMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(evidencijaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evidencija in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvidencija() throws Exception {
        // Initialize the database
        insertedEvidencija = evidencijaRepository.saveAndFlush(evidencija);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the evidencija
        restEvidencijaMockMvc
            .perform(delete(ENTITY_API_URL_ID, evidencija.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return evidencijaRepository.count();
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

    protected Evidencija getPersistedEvidencija(Evidencija evidencija) {
        return evidencijaRepository.findById(evidencija.getId()).orElseThrow();
    }

    protected void assertPersistedEvidencijaToMatchAllProperties(Evidencija expectedEvidencija) {
        assertEvidencijaAllPropertiesEquals(expectedEvidencija, getPersistedEvidencija(expectedEvidencija));
    }

    protected void assertPersistedEvidencijaToMatchUpdatableProperties(Evidencija expectedEvidencija) {
        assertEvidencijaAllUpdatablePropertiesEquals(expectedEvidencija, getPersistedEvidencija(expectedEvidencija));
    }
}
