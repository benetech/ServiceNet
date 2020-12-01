package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.repository.PhysicalAddressRepository;
import org.benetech.servicenet.service.PhysicalAddressService;
import org.benetech.servicenet.service.dto.AddressDTO;
import org.benetech.servicenet.service.mapper.PhysicalAddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PhysicalAddress.
 */
@Service
@Transactional
public class PhysicalAddressServiceImpl implements PhysicalAddressService {

    private final Logger log = LoggerFactory.getLogger(PhysicalAddressServiceImpl.class);

    private final PhysicalAddressRepository physicalAddressRepository;

    private final PhysicalAddressMapper physicalAddressMapper;

    public PhysicalAddressServiceImpl(PhysicalAddressRepository physicalAddressRepository,
                                      PhysicalAddressMapper physicalAddressMapper) {
        this.physicalAddressRepository = physicalAddressRepository;
        this.physicalAddressMapper = physicalAddressMapper;
    }

    /**
     * Save a physicalAddress.
     *
     * @param physicalAddressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AddressDTO save(AddressDTO physicalAddressDTO) {
        log.debug("Request to save PhysicalAddress : {}", physicalAddressDTO);

        PhysicalAddress physicalAddress = physicalAddressMapper.toEntity(physicalAddressDTO);
        physicalAddress = physicalAddressRepository.save(physicalAddress);
        return physicalAddressMapper.toDto(physicalAddress);
    }

    /**
     * Save a physicalAddress.
     *
     * @param physicalAddress the entity to save
     * @return the persisted entity
     */
    @Override
    public PhysicalAddress save(PhysicalAddress physicalAddress) {
        log.debug("Request to save PhysicalAddress : {}", physicalAddress);
        return physicalAddressRepository.save(physicalAddress);
    }

    /**
     * Get all the physicalAddresses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findAll() {
        log.debug("Request to get all PhysicalAddresses");
        return physicalAddressRepository.findAll().stream()
            .map(physicalAddressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the physicalAddresses on page
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PhysicalAddresses");
        return physicalAddressRepository.findAll(pageable)
            .map(physicalAddressMapper::toDto);
    }

    /**
     * Get one physicalAddress by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(UUID id) {
        log.debug("Request to get PhysicalAddress : {}", id);
        return physicalAddressRepository.findById(id)
            .map(physicalAddressMapper::toDto);
    }

    /**
     * Delete the physicalAddress by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete PhysicalAddress : {}", id);
        physicalAddressRepository.deleteById(id);
    }
}
