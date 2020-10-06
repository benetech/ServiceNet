package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.FieldsDisplaySettings}.
 */
public interface FieldsDisplaySettingsService {

    /**
     * Save a fieldsDisplaySettings.
     *
     * @param fieldsDisplaySettingsDTO the entity to save.
     * @return the persisted entity.
     */
    FieldsDisplaySettingsDTO save(FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO);

    /**
     * Get all the fieldsDisplaySettings.
     *
     * @return the list of entities.
     */
    List<FieldsDisplaySettingsDTO> findAll();

    /**
     * Get all the fieldsDisplaySettings.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<FieldsDisplaySettingsDTO> findAll(Pageable pageable);

    /**
     * Get all the fieldsDisplaySettings for current user.
     *
     * @return the list of entities.
     */
    List<FieldsDisplaySettingsDTO> findAllByCurrentUser();

    /**
     * Get all the fieldsDisplaySettings for current users system account.
     *
     * @return the list of entities.
     */
    List<FieldsDisplaySettingsDTO> findAllBySystemAccount(UUID id);

    /**
     * Check if fieldsDisplaySettings with given name exists in given system account.
     *
     * @return the list of entities.
     */
    List<FieldsDisplaySettingsDTO> findBySystemAccountAndName(String systemAccountName, String settingName);

    /**
     * Get the "id" fieldsDisplaySettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FieldsDisplaySettingsDTO> findOne(UUID id);

    /**
     * Delete the "id" fieldsDisplaySettings.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
