package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.repository.ProgramRepository;
import org.benetech.servicenet.service.ProgramService;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.mapper.ProgramMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Program.
 */
@Service
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private final Logger log = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    public ProgramServiceImpl(ProgramRepository programRepository, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
    }

    /**
     * Save a program.
     *
     * @param programDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProgramDTO save(ProgramDTO programDTO) {
        log.debug("Request to save Program : {}", programDTO);

        Program program = programMapper.toEntity(programDTO);
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    /**
     * Get all the programs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProgramDTO> findAll() {
        log.debug("Request to get all Programs");
        return programRepository.findAll().stream()
            .map(programMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the programs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Programs");
        return programRepository.findAll(pageable)
            .map(programMapper::toDto);
    }

    /**
     * Get one program by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProgramDTO> findOne(UUID id) {
        log.debug("Request to get Program : {}", id);
        return programRepository.findById(id)
            .map(programMapper::toDto);
    }

    /**
     * Delete the program by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Program : {}", id);
        programRepository.deleteById(id);
    }
}
