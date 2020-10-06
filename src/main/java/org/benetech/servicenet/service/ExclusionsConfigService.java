package org.benetech.servicenet.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.benetech.servicenet.domain.ExclusionsConfig}.
 */
public interface ExclusionsConfigService {

    /**
     * Save a exclusionsConfig.
     *
     * @param exclusionsConfigDTO the entity to save.
     * @return the persisted entity.
     */
    ExclusionsConfigDTO save(ExclusionsConfigDTO exclusionsConfigDTO);

    /**
     * Get all the exclusionsConfigs.
     *
     * @return the list of entities.
     */
    List<ExclusionsConfigDTO> findAll();

    /**
     * Get all the exclusionsConfigs.
     *
     * @param pageable the pagination information
     * @return the list of entities.
     */
    Page<ExclusionsConfigDTO> findAll(Pageable pageable);

    Map<UUID, ExclusionsConfig> getAllBySystemAccountId();

    List<ExclusionsConfig> findAllBySystemAccountName(String systemAccountName);

    /**
     * Get the "id" exclusionsConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExclusionsConfigDTO> findOne(UUID id);

    /**
     * Delete the "id" exclusionsConfig.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
