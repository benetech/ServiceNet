package org.benetech.servicenet.service.mapper;


import java.util.UUID;
import org.benetech.servicenet.domain.*;
import org.benetech.servicenet.service.dto.ReferralDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Referral} and its DTO {@link ReferralDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, BeneficiaryMapper.class, LocationMapper.class})
public interface ReferralMapper extends EntityMapper<ReferralDTO, Referral> {

    @Mapping(source = "from.id", target = "fromId")
    @Mapping(source = "from.name", target = "fromName")
    @Mapping(source = "to.id", target = "toId")
    @Mapping(source = "to.name", target = "toName")
    @Mapping(source = "fromLocation.id", target = "fromLocationId")
    @Mapping(source = "fromLocation.name", target = "fromLocationName")
    @Mapping(source = "toLocation.id", target = "toLocationId")
    @Mapping(source = "toLocation.name", target = "toLocationName")
    @Mapping(source = "beneficiary.id", target = "beneficiaryId")
    @Mapping(source = "beneficiary.identifier", target = "beneficiaryIdentifier")
    @Mapping(source = "beneficiary.phoneNumber", target = "beneficiaryPhoneNumber")
    ReferralDTO toDto(Referral referral);

    @Mapping(source = "fromId", target = "from")
    @Mapping(source = "toId", target = "to")
    @Mapping(source = "fromLocationId", target = "fromLocation")
    @Mapping(source = "toLocationId", target = "toLocation")
    @Mapping(source = "beneficiaryId", target = "beneficiary")
    Referral toEntity(ReferralDTO referralDTO);

    default Referral fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Referral referral = new Referral();
        referral.setId(id);
        return referral;
    }
}
