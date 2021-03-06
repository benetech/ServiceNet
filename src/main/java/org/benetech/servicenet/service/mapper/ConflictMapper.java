package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Conflict and its DTO ConflictDTO.
 */
@Mapper(componentModel = "spring", uses = {SystemAccountMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ConflictMapper extends EntityMapper<ConflictDTO, Conflict> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "partner.id", target = "partnerId")
    @Mapping(source = "partner.name", target = "partnerName")
    ConflictDTO toDto(Conflict conflict);

    @Mapping(source = "ownerId", target = "owner")
    Conflict toEntity(ConflictDTO conflictDTO);

    default Conflict fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Conflict conflict = new Conflict();
        conflict.setId(id);
        return conflict;
    }
}
