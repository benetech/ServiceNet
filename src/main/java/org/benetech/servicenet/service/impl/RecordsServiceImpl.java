package org.benetech.servicenet.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.FieldExclusion;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.service.ConflictService;
import org.benetech.servicenet.service.ExclusionsConfigService;
import org.benetech.servicenet.service.FieldExclusionService;
import org.benetech.servicenet.service.OrganizationService;
import org.benetech.servicenet.service.RecordsService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.benetech.servicenet.service.dto.FieldExclusionDTO;
import org.benetech.servicenet.service.dto.RecordDTO;
import org.benetech.servicenet.service.mapper.OrganizationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private ExclusionsConfigService exclusionsConfigService;

    @Autowired
    private FieldExclusionService fieldExclusionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConflictService conflictService;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Optional<RecordDTO> getRecordFromOrganization(UUID organizationId, UUID resourceId) {
        return organizationService.findOne(organizationId)
            .flatMap(organization -> userService.getCurrentSystemAccount()
                .flatMap(account -> getFilteredResult(resourceId, organization)));
    }

    private Optional<RecordDTO> getFilteredResult(UUID resourceId, Organization organization) {
        Optional<ExclusionsConfigDTO> config = exclusionsConfigService.findOneBySystemAccountName(
            organization.getAccount().getName());
        List<ConflictDTO> conflictDTOS = conflictService.findAllWithResourceId(resourceId);
        List<ConflictDTO> filteredByPartners = filterWithPartnersConfigs(conflictDTOS);
        return config.map(conf -> filterExclusions(organization, conf, filteredByPartners))
            .orElse(Optional.of(new RecordDTO(organizationMapper.toDto(organization), new HashSet<>(), filteredByPartners)));
    }

    private List<ConflictDTO> filterWithPartnersConfigs(List<ConflictDTO> conflictDTOS) {
        Set<String> conflictingProviders = conflictDTOS.stream()
            .flatMap(c -> c.getAcceptedThisChangeNames().stream())
            .collect(Collectors.toSet());

        List<ExclusionsConfigDTO> configs = exclusionsConfigService.findAllBySystemAccountNameIn(conflictingProviders);

        for (ExclusionsConfigDTO config : configs) {
            filterConflictsByConfig(conflictDTOS, config);
        }

        return conflictDTOS;
    }

    private void filterConflictsByConfig(List<ConflictDTO> conflictDTOS, ExclusionsConfigDTO config) {
        Set<FieldExclusion> excludedFields = fieldExclusionService.findAllByConfigId(config.getId());
        Iterator<ConflictDTO> conflictsIterator = conflictDTOS.iterator();
        while (conflictsIterator.hasNext()) {
            filterConflictByConfig(config, excludedFields, conflictsIterator);
        }
    }

    private void filterConflictByConfig(ExclusionsConfigDTO config, Set<FieldExclusion> excludedFields,
                                        Iterator<ConflictDTO> conflictsIterator) {
        ConflictDTO conflictDTO = conflictsIterator.next();
        for (FieldExclusion exclusion : excludedFields) {
            if (shouldConflictBeFiltered(config, conflictDTO, exclusion)) {
                removeAccountFromConflict(config, conflictsIterator, conflictDTO);
            }
        }
    }

    private void removeAccountFromConflict(ExclusionsConfigDTO config, Iterator<ConflictDTO> conflictsIterator,
                                           ConflictDTO conflictDTO) {
        if (isOnlyOneAccepting(conflictDTO)) {
            conflictsIterator.remove();
        } else {
            conflictDTO.removeAcceptedThisChange(config.getAccountName());
        }
    }

    private boolean isOnlyOneAccepting(ConflictDTO conflictDTO) {
        return conflictDTO.getAcceptedThisChange().size() == 1;
    }

    private boolean shouldConflictBeFiltered(ExclusionsConfigDTO config, ConflictDTO conflictDTO, FieldExclusion exclusion) {
        return conflictDTO.getAcceptedThisChangeNames().contains(config.getAccountName())
            && exclusion.getEntity().equals(conflictDTO.getEntityPath())
            && exclusion.getExcludedFields().contains(conflictDTO.getFieldName());
    }

    private Optional<RecordDTO> filterExclusions(Organization organization, ExclusionsConfigDTO config,
                                                 List<ConflictDTO> conflictDTOS) {
        try {
            Organization filteredOrg = excludeOrganizationFields(organization, config);
            List<ConflictDTO> filteredConflicts = excludeConflicts(conflictDTOS, config);
            Set<FieldExclusionDTO> excludedFields = fieldExclusionService.findAllDTOByConfigId(config.getId());
            return Optional.of(new RecordDTO(organizationMapper.toDto(filteredOrg), excludedFields, filteredConflicts));
        } catch (IllegalAccessException e) {
            log.error("Unable to filter record.");
            return Optional.empty();
        }
    }

    private List<ConflictDTO> excludeConflicts(List<ConflictDTO> conflictDTOS, ExclusionsConfigDTO config) {
        Set<String> excludedOrgFields = getExcludedFields(config.getId(), Organization.class);

        return conflictDTOS.stream()
            .filter(c -> !excludedOrgFields.contains(c.getFieldName()))
            .collect(Collectors.toList());
    }

    private Organization excludeOrganizationFields(Organization organization, ExclusionsConfigDTO config)
        throws IllegalAccessException {
        Set<String> excludedNames = getExcludedFields(config.getId(), Organization.class);
        resetExcludedFields(organization, excludedNames, Organization.class);
        return organization;
    }

    private void resetExcludedFields(Object object, Set<String> excludedNames, Class clazz) throws IllegalAccessException {
        for (Field field : clazz.getDeclaredFields()) {
            if (excludedNames.contains(field.getName())) {
                resetField(object, field);
            }
        }
    }

    private void resetField(Object object, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, null);
    }

    private Set<String> getExcludedFields(UUID configId, Class clazz) {
        return fieldExclusionService.findAllByConfigId(configId).stream()
            .filter(x -> isExcludedClass(x, clazz))
            .flatMap(x -> x.getExcludedFields().stream())
            .collect(Collectors.toSet());
    }

    private boolean isExcludedClass(FieldExclusion exclusion, Class clazz) {
        return exclusion.getEntity().equals(clazz.getCanonicalName());
    }
}