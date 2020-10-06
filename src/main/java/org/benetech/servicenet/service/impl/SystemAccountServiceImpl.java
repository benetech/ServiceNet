package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.repository.SystemAccountRepository;
import org.benetech.servicenet.service.SystemAccountService;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.benetech.servicenet.service.mapper.SystemAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing SystemAccount.
 */
@Service
@Transactional
public class SystemAccountServiceImpl implements SystemAccountService {

    private final Logger log = LoggerFactory.getLogger(SystemAccountServiceImpl.class);

    private final SystemAccountRepository systemAccountRepository;

    private final SystemAccountMapper systemAccountMapper;

    public SystemAccountServiceImpl(SystemAccountRepository systemAccountRepository,
                                    SystemAccountMapper systemAccountMapper) {
        this.systemAccountRepository = systemAccountRepository;
        this.systemAccountMapper = systemAccountMapper;
    }

    @Override
    public SystemAccountDTO save(SystemAccountDTO systemAccountDTO) {
        log.debug("Request to save SystemAccount : {}", systemAccountDTO);

        SystemAccount systemAccount = systemAccountMapper.toEntity(systemAccountDTO);
        systemAccount = systemAccountRepository.save(systemAccount);
        return systemAccountMapper.toDto(systemAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemAccountDTO> findAll() {
        log.debug("Request to get all SystemAccounts");
        return systemAccountRepository.findAll().stream()
            .map(systemAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemAccounts");
        return systemAccountRepository.findAll(pageable)
            .map(systemAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemAccountDTO> findOne(UUID id) {
        log.debug("Request to get SystemAccount : {}", id);
        return systemAccountRepository.findById(id)
            .map(systemAccountMapper::toDto);
    }

    public void delete(UUID id) {
        log.debug("Request to delete SystemAccount : {}", id);
        systemAccountRepository.deleteById(id);
    }

    @Override
    public Optional<SystemAccount> findByName(String name) {
        return systemAccountRepository.findByName(name);
    }

}
