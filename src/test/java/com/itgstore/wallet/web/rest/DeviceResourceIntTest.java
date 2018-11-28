package com.itgstore.wallet.web.rest;

import com.itgstore.wallet.WalletApp;

import com.itgstore.wallet.domain.Device;
import com.itgstore.wallet.domain.Facture;
import com.itgstore.wallet.domain.Operation;
import com.itgstore.wallet.domain.Client;
import com.itgstore.wallet.repository.DeviceRepository;
import com.itgstore.wallet.service.DeviceService;
import com.itgstore.wallet.service.dto.DeviceDTO;
import com.itgstore.wallet.service.mapper.DeviceMapper;
import com.itgstore.wallet.web.rest.errors.ExceptionTranslator;
import com.itgstore.wallet.service.dto.DeviceCriteria;
import com.itgstore.wallet.service.DeviceQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static com.itgstore.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DeviceResource REST controller.
 *
 * @see DeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApp.class)
public class DeviceResourceIntTest {

    private static final String DEFAULT_DEVICE_MARK = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_MARK = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_OS = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_OS = "BBBBBBBBBB";

    private static final Long DEFAULT_DEVICE_NUMBER = 1L;
    private static final Long UPDATED_DEVICE_NUMBER = 2L;

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeviceMockMvc;

    private Device device;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceResource deviceResource = new DeviceResource(deviceService, deviceQueryService);
        this.restDeviceMockMvc = MockMvcBuilders.standaloneSetup(deviceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device()
            .deviceMark(DEFAULT_DEVICE_MARK)
            .deviceOs(DEFAULT_DEVICE_OS)
            .deviceNumber(DEFAULT_DEVICE_NUMBER)
            .token(DEFAULT_TOKEN);
        // Add required entity
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        device.setClient(client);
        return device;
    }

    @Before
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getDeviceMark()).isEqualTo(DEFAULT_DEVICE_MARK);
        assertThat(testDevice.getDeviceOs()).isEqualTo(DEFAULT_DEVICE_OS);
        assertThat(testDevice.getDeviceNumber()).isEqualTo(DEFAULT_DEVICE_NUMBER);
        assertThat(testDevice.getToken()).isEqualTo(DEFAULT_TOKEN);
    }

    @Test
    @Transactional
    public void createDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setToken(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc.perform(post("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc.perform(get("/api/devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceMark").value(hasItem(DEFAULT_DEVICE_MARK.toString())))
            .andExpect(jsonPath("$.[*].deviceOs").value(hasItem(DEFAULT_DEVICE_OS.toString())))
            .andExpect(jsonPath("$.[*].deviceNumber").value(hasItem(DEFAULT_DEVICE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())));
    }
    
    @Test
    @Transactional
    public void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.deviceMark").value(DEFAULT_DEVICE_MARK.toString()))
            .andExpect(jsonPath("$.deviceOs").value(DEFAULT_DEVICE_OS.toString()))
            .andExpect(jsonPath("$.deviceNumber").value(DEFAULT_DEVICE_NUMBER.intValue()))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN.toString()));
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceMarkIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceMark equals to DEFAULT_DEVICE_MARK
        defaultDeviceShouldBeFound("deviceMark.equals=" + DEFAULT_DEVICE_MARK);

        // Get all the deviceList where deviceMark equals to UPDATED_DEVICE_MARK
        defaultDeviceShouldNotBeFound("deviceMark.equals=" + UPDATED_DEVICE_MARK);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceMarkIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceMark in DEFAULT_DEVICE_MARK or UPDATED_DEVICE_MARK
        defaultDeviceShouldBeFound("deviceMark.in=" + DEFAULT_DEVICE_MARK + "," + UPDATED_DEVICE_MARK);

        // Get all the deviceList where deviceMark equals to UPDATED_DEVICE_MARK
        defaultDeviceShouldNotBeFound("deviceMark.in=" + UPDATED_DEVICE_MARK);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceMarkIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceMark is not null
        defaultDeviceShouldBeFound("deviceMark.specified=true");

        // Get all the deviceList where deviceMark is null
        defaultDeviceShouldNotBeFound("deviceMark.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceOsIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceOs equals to DEFAULT_DEVICE_OS
        defaultDeviceShouldBeFound("deviceOs.equals=" + DEFAULT_DEVICE_OS);

        // Get all the deviceList where deviceOs equals to UPDATED_DEVICE_OS
        defaultDeviceShouldNotBeFound("deviceOs.equals=" + UPDATED_DEVICE_OS);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceOsIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceOs in DEFAULT_DEVICE_OS or UPDATED_DEVICE_OS
        defaultDeviceShouldBeFound("deviceOs.in=" + DEFAULT_DEVICE_OS + "," + UPDATED_DEVICE_OS);

        // Get all the deviceList where deviceOs equals to UPDATED_DEVICE_OS
        defaultDeviceShouldNotBeFound("deviceOs.in=" + UPDATED_DEVICE_OS);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceOsIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceOs is not null
        defaultDeviceShouldBeFound("deviceOs.specified=true");

        // Get all the deviceList where deviceOs is null
        defaultDeviceShouldNotBeFound("deviceOs.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceNumber equals to DEFAULT_DEVICE_NUMBER
        defaultDeviceShouldBeFound("deviceNumber.equals=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceList where deviceNumber equals to UPDATED_DEVICE_NUMBER
        defaultDeviceShouldNotBeFound("deviceNumber.equals=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceNumber in DEFAULT_DEVICE_NUMBER or UPDATED_DEVICE_NUMBER
        defaultDeviceShouldBeFound("deviceNumber.in=" + DEFAULT_DEVICE_NUMBER + "," + UPDATED_DEVICE_NUMBER);

        // Get all the deviceList where deviceNumber equals to UPDATED_DEVICE_NUMBER
        defaultDeviceShouldNotBeFound("deviceNumber.in=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceNumber is not null
        defaultDeviceShouldBeFound("deviceNumber.specified=true");

        // Get all the deviceList where deviceNumber is null
        defaultDeviceShouldNotBeFound("deviceNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceNumber greater than or equals to DEFAULT_DEVICE_NUMBER
        defaultDeviceShouldBeFound("deviceNumber.greaterOrEqualThan=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceList where deviceNumber greater than or equals to UPDATED_DEVICE_NUMBER
        defaultDeviceShouldNotBeFound("deviceNumber.greaterOrEqualThan=" + UPDATED_DEVICE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDevicesByDeviceNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where deviceNumber less than or equals to DEFAULT_DEVICE_NUMBER
        defaultDeviceShouldNotBeFound("deviceNumber.lessThan=" + DEFAULT_DEVICE_NUMBER);

        // Get all the deviceList where deviceNumber less than or equals to UPDATED_DEVICE_NUMBER
        defaultDeviceShouldBeFound("deviceNumber.lessThan=" + UPDATED_DEVICE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDevicesByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where token equals to DEFAULT_TOKEN
        defaultDeviceShouldBeFound("token.equals=" + DEFAULT_TOKEN);

        // Get all the deviceList where token equals to UPDATED_TOKEN
        defaultDeviceShouldNotBeFound("token.equals=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    public void getAllDevicesByTokenIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where token in DEFAULT_TOKEN or UPDATED_TOKEN
        defaultDeviceShouldBeFound("token.in=" + DEFAULT_TOKEN + "," + UPDATED_TOKEN);

        // Get all the deviceList where token equals to UPDATED_TOKEN
        defaultDeviceShouldNotBeFound("token.in=" + UPDATED_TOKEN);
    }

    @Test
    @Transactional
    public void getAllDevicesByTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where token is not null
        defaultDeviceShouldBeFound("token.specified=true");

        // Get all the deviceList where token is null
        defaultDeviceShouldNotBeFound("token.specified=false");
    }

    @Test
    @Transactional
    public void getAllDevicesByFactureDevicePayeurIsEqualToSomething() throws Exception {
        // Initialize the database
        Facture factureDevicePayeur = FactureResourceIntTest.createEntity(em);
        em.persist(factureDevicePayeur);
        em.flush();
        device.addFactureDevicePayeur(factureDevicePayeur);
        deviceRepository.saveAndFlush(device);
        Long factureDevicePayeurId = factureDevicePayeur.getId();

        // Get all the deviceList where factureDevicePayeur equals to factureDevicePayeurId
        defaultDeviceShouldBeFound("factureDevicePayeurId.equals=" + factureDevicePayeurId);

        // Get all the deviceList where factureDevicePayeur equals to factureDevicePayeurId + 1
        defaultDeviceShouldNotBeFound("factureDevicePayeurId.equals=" + (factureDevicePayeurId + 1));
    }


    @Test
    @Transactional
    public void getAllDevicesByOperationDeviceEmetteurIsEqualToSomething() throws Exception {
        // Initialize the database
        Operation operationDeviceEmetteur = OperationResourceIntTest.createEntity(em);
        em.persist(operationDeviceEmetteur);
        em.flush();
        device.addOperationDeviceEmetteur(operationDeviceEmetteur);
        deviceRepository.saveAndFlush(device);
        Long operationDeviceEmetteurId = operationDeviceEmetteur.getId();

        // Get all the deviceList where operationDeviceEmetteur equals to operationDeviceEmetteurId
        defaultDeviceShouldBeFound("operationDeviceEmetteurId.equals=" + operationDeviceEmetteurId);

        // Get all the deviceList where operationDeviceEmetteur equals to operationDeviceEmetteurId + 1
        defaultDeviceShouldNotBeFound("operationDeviceEmetteurId.equals=" + (operationDeviceEmetteurId + 1));
    }


    @Test
    @Transactional
    public void getAllDevicesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        device.setClient(client);
        deviceRepository.saveAndFlush(device);
        Long clientId = client.getId();

        // Get all the deviceList where client equals to clientId
        defaultDeviceShouldBeFound("clientId.equals=" + clientId);

        // Get all the deviceList where client equals to clientId + 1
        defaultDeviceShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc.perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceMark").value(hasItem(DEFAULT_DEVICE_MARK.toString())))
            .andExpect(jsonPath("$.[*].deviceOs").value(hasItem(DEFAULT_DEVICE_OS.toString())))
            .andExpect(jsonPath("$.[*].deviceNumber").value(hasItem(DEFAULT_DEVICE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN.toString())));

        // Check, that the count call also returns 1
        restDeviceMockMvc.perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc.perform(get("/api/devices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc.perform(get("/api/devices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get("/api/devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .deviceMark(UPDATED_DEVICE_MARK)
            .deviceOs(UPDATED_DEVICE_OS)
            .deviceNumber(UPDATED_DEVICE_NUMBER)
            .token(UPDATED_TOKEN);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getDeviceMark()).isEqualTo(UPDATED_DEVICE_MARK);
        assertThat(testDevice.getDeviceOs()).isEqualTo(UPDATED_DEVICE_OS);
        assertThat(testDevice.getDeviceNumber()).isEqualTo(UPDATED_DEVICE_NUMBER);
        assertThat(testDevice.getToken()).isEqualTo(UPDATED_TOKEN);
    }

    @Test
    @Transactional
    public void updateNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc.perform(put("/api/devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Get the device
        restDeviceMockMvc.perform(delete("/api/devices/{id}", device.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Device.class);
        Device device1 = new Device();
        device1.setId(1L);
        Device device2 = new Device();
        device2.setId(device1.getId());
        assertThat(device1).isEqualTo(device2);
        device2.setId(2L);
        assertThat(device1).isNotEqualTo(device2);
        device1.setId(null);
        assertThat(device1).isNotEqualTo(device2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceDTO.class);
        DeviceDTO deviceDTO1 = new DeviceDTO();
        deviceDTO1.setId(1L);
        DeviceDTO deviceDTO2 = new DeviceDTO();
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO2.setId(deviceDTO1.getId());
        assertThat(deviceDTO1).isEqualTo(deviceDTO2);
        deviceDTO2.setId(2L);
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
        deviceDTO1.setId(null);
        assertThat(deviceDTO1).isNotEqualTo(deviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(deviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(deviceMapper.fromId(null)).isNull();
    }
}
