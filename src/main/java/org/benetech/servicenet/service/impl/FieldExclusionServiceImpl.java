package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.repository.FieldExclusionRepository;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.service.mapper.FieldExclusionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FieldExclusion.
 */
@Service
@Transactional
public class FieldExclusionServiceImpl implements FieldExclusionService {

    private final Logger log = LoggerFactory.getLogger(FieldExclusionServiceImpl.class);

    private final FieldExclusionRepository fieldExclusionRepository;

    private final FieldExclusionMapper fieldExclusionMapper;

    public FieldExclusionServiceImpl(FieldExclusionRepository fieldExclusionRepository,
                                     FieldExclusionMapper fieldExclusionMapper) {
        this.fieldExclusionRepository = fieldExclusionRepository;
        this.fieldExclusionMapper = fieldExclusionMapper;
    }

    @Override
    public FieldExclusionDTO save(FieldExclusionDTO fieldExclusionDTO) {
        log.debug("Request to save FieldExclusion : {}", fieldExclusionDTO);

        FieldExclusion fieldExclusion = fieldExclusionMapper.toEntity(fieldExclusionDTO);
        fieldExclusion = fieldExclusionRepository.save(fieldExclusion);
        return fieldExclusionMapper.toDto(fieldExclusion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FieldExclusionDTO> findAll() {
        log.debug("Request to get all FieldExclusions");
        return fieldExclusionRepository.findAll().stream()
            .map(fieldExclusionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FieldExclusionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FieldExclusions");
        return fieldExclusionRepository.findAll(pageable)
            .map(fieldExclusionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldExclusionDTO> findOne(UUID id) {
        log.debug("Request to get FieldExclusion : {}", id);
        return fieldExclusionRepository.findById(id)
            .map(fieldExclusionMapper::toDto);
    }

    @Override
    public Set<FieldExclusionDTO> findAllDTOByConfigId(UUID configId) {
        return findAllByConfigId(configId).stream()
            .map(fieldExclusionMapper::toDto)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<FieldExclusion> findAllByConfigId(UUID configId) {
        return fieldExclusionRepository.findAllByConfigId(configId);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete FieldExclusion : {}", id);
        fieldExclusionRepository.deleteById(id);
    }
}
