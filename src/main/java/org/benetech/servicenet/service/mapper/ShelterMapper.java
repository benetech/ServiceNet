package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.service.GeocodingResultService;
import org.benetech.servicenet.service.dto.ShelterDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Shelter and its DTO ShelterDTO.
 */
@Component
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public abstract class ShelterMapper {

    @Autowired
    private GeocodingResultService geocodingResultService;

    public abstract ShelterDTO toDto(Shelter shelter);

    public abstract Shelter toEntity(ShelterDTO shelterDTO);

    public ShelterDTO toGeocodedDto(Shelter shelter) {
        ShelterDTO dto = toDto(shelter);
        if (StringUtils.isNotBlank(shelter.getAddress())) {
            List<GeocodingResult> geocodingResults = geocodingResultService
                .findAllForAddressOrFetchIfEmpty(shelter);
            dto.setGeocodingResults(geocodingResults);
        }
        return dto;
    }

    Shelter fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Shelter shelter = new Shelter();
        shelter.setId(id);
        return shelter;
    }
}
