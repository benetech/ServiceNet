package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FieldExclusion.
 */
public interface FieldExclusionService {

    /**
     * Save a fieldExclusion.
     *
     * @param fieldExclusionDTO the entity to save
     * @return the persisted entity
     */
    FieldExclusionDTO save(FieldExclusionDTO fieldExclusionDTO);

    /**
     * Get all the fieldExclusions.
     *
     * @return the list of entities
     */
    List<FieldExclusionDTO> findAll();

    /**
     * Get all the fieldExclusions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FieldExclusionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fieldExclusion.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FieldExclusionDTO> findOne(UUID id);

    Set<FieldExclusion> findAllByConfigId(UUID configId);

    Set<FieldExclusionDTO> findAllDTOByConfigId(UUID configId);

    /**
     * Delete the "id" fieldExclusion.
     *
     * @param id the id of the entity
     */
    void delete(UUID id);
}
