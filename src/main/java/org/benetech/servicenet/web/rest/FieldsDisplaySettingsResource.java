package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.service.FieldsDisplaySettingsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link org.benetech.servicenet.domain.FieldsDisplaySettings}.
 */
@RestController
@RequestMapping("/api")
public class FieldsDisplaySettingsResource {

    private final Logger log = LoggerFactory.getLogger(FieldsDisplaySettingsResource.class);

    private static final String ENTITY_NAME = "fieldsDisplaySettings";

    private final FieldsDisplaySettingsService fieldsDisplaySettingsService;

    private final UserService userService;

    public FieldsDisplaySettingsResource(
        FieldsDisplaySettingsService fieldsDisplaySettingsService,
        UserService userService
    ) {
        this.fieldsDisplaySettingsService = fieldsDisplaySettingsService;
        this.userService = userService;
    }

    /**
     * {@code POST  /fields-display-settings} : Create a new fieldsDisplaySettings.
     *
     * @param fieldsDisplaySettingsDTO the fieldsDisplaySettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fieldsDisplaySettingsDTO,
     * or with status {@code 400 (Bad Request)} if the fieldsDisplaySettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fields-display-settings")
    public ResponseEntity<FieldsDisplaySettingsDTO> createFieldsDisplaySettings(
        @Valid @RequestBody FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FieldsDisplaySettings : {}", fieldsDisplaySettingsDTO);
        if (fieldsDisplaySettingsDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new fieldsDisplaySettings cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        String systemAccountName = userService.getCurrentSystemAccountName();
        List<FieldsDisplaySettingsDTO> existingSettings = fieldsDisplaySettingsService
            .findBySystemAccountAndName(systemAccountName, fieldsDisplaySettingsDTO.getName());
        if (existingSettings.size() > 0) {
            throw new BadRequestAlertException(
                "Setting with this name already exists in your organization. Please change the name.",
                ENTITY_NAME,
                "settingNameExists"
            );
        }
        FieldsDisplaySettingsDTO result = fieldsDisplaySettingsService.save(fieldsDisplaySettingsDTO);
        return ResponseEntity.created(new URI("/api/fields-display-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fields-display-settings} : Updates an existing fieldsDisplaySettings.
     *
     * @param fieldsDisplaySettingsDTO the fieldsDisplaySettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fieldsDisplaySettingsDTO,
     * or with status {@code 400 (Bad Request)} if the fieldsDisplaySettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fieldsDisplaySettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fields-display-settings")
    public ResponseEntity<FieldsDisplaySettingsDTO> updateFieldsDisplaySettings(
        @Valid @RequestBody FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FieldsDisplaySettings : {}", fieldsDisplaySettingsDTO);
        if (fieldsDisplaySettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FieldsDisplaySettingsDTO result = fieldsDisplaySettingsService.save(fieldsDisplaySettingsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fieldsDisplaySettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fields-display-settings} : get all the fieldsDisplaySettings.
     *
     * @param pageable the pagination information
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldsDisplaySettings in body.
     */
    @GetMapping("/fields-display-settings")
    public ResponseEntity<List<FieldsDisplaySettingsDTO>> getAllFieldsDisplaySettings(
        Pageable pageable) {
        log.debug("REST request to get all FieldsDisplaySettings");
        Page<FieldsDisplaySettingsDTO> page = fieldsDisplaySettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fields-display-settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET  /fields-display-settings-by-user} : get all the fieldsDisplaySettings for current user.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldsDisplaySettings in body.
     */
    @GetMapping("/fields-display-settings-by-user")
    public List<FieldsDisplaySettingsDTO> getAllUserFieldsDisplaySettings() {
        log.debug("REST request to get all FieldsDisplaySettings by current user");
        return fieldsDisplaySettingsService.findAllByCurrentUser();
    }

    /**
     * {@code GET  /fields-display-settings-by-system-account} : get all the fieldsDisplaySettings from
     * current users system account.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fieldsDisplaySettings in body.
     */
    @GetMapping("/fields-display-settings-by-system-account")
    public List<FieldsDisplaySettingsDTO> getAllSystemAccountFieldsDisplaySettings() {
        log.debug("REST request to get all FieldsDisplaySettings by system accounts");
        Optional<SystemAccount> accountOpt = userService.getCurrentSystemAccount();
        UUID systemAccountId = accountOpt.map(SystemAccount::getId).orElse(null);
        return fieldsDisplaySettingsService.findAllBySystemAccount(systemAccountId);
    }

    /**
     * {@code GET  /fields-display-settings/:id} : get the "id" fieldsDisplaySettings.
     *
     * @param id the id of the fieldsDisplaySettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fieldsDisplaySettingsDTO, or
     * with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fields-display-settings/{id}")
    public ResponseEntity<FieldsDisplaySettingsDTO> getFieldsDisplaySettings(@PathVariable UUID id) {
        log.debug("REST request to get FieldsDisplaySettings : {}", id);
        Optional<FieldsDisplaySettingsDTO> fieldsDisplaySettingsDTO = fieldsDisplaySettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fieldsDisplaySettingsDTO);
    }

    /**
     * {@code DELETE  /fields-display-settings/:id} : delete the "id" fieldsDisplaySettings.
     *
     * @param id the id of the fieldsDisplaySettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fields-display-settings/{id}")
    public ResponseEntity<Void> deleteFieldsDisplaySettings(@PathVariable UUID id) {
        log.debug("REST request to delete FieldsDisplaySettings : {}", id);
        fieldsDisplaySettingsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
