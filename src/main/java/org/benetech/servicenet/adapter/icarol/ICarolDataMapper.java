package org.benetech.servicenet.adapter.icarol;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolAccessibility;
import org.benetech.servicenet.adapter.icarol.model.ICarolAgency;
import org.benetech.servicenet.adapter.icarol.model.ICarolContact;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolDay;
import org.benetech.servicenet.adapter.icarol.model.ICarolHours;
import org.benetech.servicenet.adapter.icarol.model.ICarolProgram;
import org.benetech.servicenet.adapter.icarol.model.ICarolSite;
import org.benetech.servicenet.adapter.icarol.model.ICarolTaxonomy;
import org.benetech.servicenet.adapter.shared.MapperUtils;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.benetech.servicenet.adapter.shared.util.OpeningHoursUtils;
import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.OpeningHours;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.PostalAddress;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.Taxonomy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mapper for ICarolData objects
 *
 * IMPORTANT: Some objects might be confidential and we should not lose that information.
 *            Each mapping method should use auto-generated version, or should set this information manually.
 *            @see ICarolConfidentialFieldsMapper for mapping fields that might be confidential as well as the whole object.
 */
@SuppressWarnings("CPD-START")
@Mapper(unmappedTargetPolicy = IGNORE)
public interface ICarolDataMapper extends ICarolConfidentialFieldsMapper {

    String LISTS_DELIMITER = "; ";

    ICarolDataMapper INSTANCE = Mappers.getMapper(ICarolDataMapper.class);
    String PRIMARY = "Primary";
    String PHONE_NUMBER = "PhoneNumber";
    String PHYSICAL_LOCATION = "PhysicalLocation";
    String POSTAL_ADDRESS = "PostalAddress";
    String ACTIVE = "Active";
    String NAME = "name";
    String URL = "url";

    Logger LOG = LoggerFactory.getLogger(ICarolDataMapper.class);

    @Mapping(source = "disabled", target = "accessibility")
    @Mapping(source = "description", target = "details")
    @Mapping(target = "id", ignore = true)
    AccessibilityForDisabilities mapAccessibility(ICarolAccessibility accessibility);

    @Mapping(source = "contact.line1", target = "address1", defaultValue = "N/A")
    @Mapping(source = "contact.zipPostalCode", target = "postalCode", defaultValue = "N/A")
    @Mapping(source = "contact.country", target = "country", defaultValue = "N/A")
    @Mapping(source = "contact.stateProvince", target = "stateProvince", defaultValue = "N/A")
    @Mapping(source = "contact.city", target = "city", defaultValue = "N/A")
    @Mapping(source = "contact.county", target = "region", defaultValue = "N/A")
    @Mapping(target = "id", ignore = true)
    PhysicalAddress mapToPhysicalAddress(ICarolContactDetails details);

    default PostalAddress mapToPostalAddress(ICarolContactDetails details) {
        if (StringUtils.isBlank(details.getContact().getStateProvince())
            || StringUtils.isBlank(details.getContact().getCity())
            || StringUtils.isBlank(details.getContact().getLine1())) {
            LOG.warn("Missing required postal address information");
            return null;
        }

        return toPostalAddress(details);
    }

    @Mapping(target = "name", source = "contact", qualifiedByName = "locationName")
    @Mapping(target = "description", source = "contact.description")
    @Mapping(target = "longitude", source = "contact.longitude")
    @Mapping(target = "latitude", source = "contact.latitude")
    @Mapping(target = "id", ignore = true)
    Location mapToLocation(ICarolContactDetails details);

    default Phone mapContactToPhone(ICarolContactDetails details) {
        if (StringUtils.isBlank(details.getContact().getNumber())) {
            LOG.warn("Missing phone number");
            return null;
        }

        return toPhone(details);
    }

    @Mapping(target = "name", source = "names", qualifiedByName = "name")
    @Mapping(target = "alternateName", source = "names", qualifiedByName = "alternateName")
    @Mapping(target = "email", source = "contactDetails", qualifiedByName = "email")
    @Mapping(target = "url", source = "contactDetails", qualifiedByName = "url")
    @Mapping(target = "description", source = "descriptionText")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "lastVerifiedOn", source = "lastVerifiedOn")
    @Mapping(target = "id", ignore = true)
    Organization mapOrganization(ICarolAgency agency);

    @Mapping(target = "name", source = "names", qualifiedByName = "name")
    @Mapping(target = "alternateName", source = "names", qualifiedByName = "alternateName")
    @Mapping(target = "email", source = "contactDetails", qualifiedByName = "email")
    @Mapping(target = "url", source = "contactDetails", qualifiedByName = "url")
    @Mapping(target = "externalDbId", source = "id")
    @Mapping(target = "description", source = "descriptionText")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastVerifiedOn", source = "lastVerifiedOn")
    @Mapping(target = "eligibility", ignore = true)
    Service mapService(ICarolProgram program);

    @Mapping(target = "id", ignore = true)
    Eligibility mapEligibility(ICarolProgram program);

    @Mapping(target = "weekday", source = "dayOfWeek", qualifiedByName = "weekday")
    @Mapping(target = "opensAt", source = "opens", qualifiedByName = "mapTime")
    @Mapping(target = "closesAt", source = "closes", qualifiedByName = "mapTime")
    @Mapping(target = "id", ignore = true)
    OpeningHours mapOpeningHours(ICarolDay day);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taxonomyId", source = "code")
    @Mapping(target = "externalDbId", source = "code")
    @Mapping(target = "details", source = "definition")
    Taxonomy mapTaxonomy(ICarolTaxonomy taxonomy);

    @Named("mapTime")
    default String mapTime(String time) {
        return OpeningHoursUtils.normalizeTime(time);
    }

    default Location mapToLocation(ICarolContactDetails details, ZonedDateTime lastVerifiedOn, String dbId,
        String providerName) {
        Location result = mapToLocation(details);
        result.setExternalDbId(dbId);
        result.setProviderName(providerName);
        result.setLastVerifiedOn(lastVerifiedOn);
        return result;
    }

    default Taxonomy extractTaxonomy(ICarolTaxonomy iCarolTaxonomy, String providerName) {
        if (iCarolTaxonomy == null) {
            return null;
        }

        Taxonomy taxonomy = mapTaxonomy(iCarolTaxonomy);

        if (StringUtils.isNotBlank(iCarolTaxonomy.getParentCode())) {
            taxonomy.setParent(new Taxonomy()
                .taxonomyId(iCarolTaxonomy.getParentCode())
                .externalDbId(iCarolTaxonomy.getParentCode())
                .providerName(providerName));
        }

        return taxonomy.providerName(providerName);
    }

    @Named("locationName")
    default String extractLocationNameIfNotConfidential(ICarolContact contact) {
        return LocationUtils.buildLocationName(contact.getCity(), contact.getStateProvince(), contact.getLine1());
    }

    default Organization extractOrganization(ICarolAgency agency, String providerName) {
        Organization result = mapOrganization(agency);

        if (StringUtils.isBlank(result.getName())) {
            LOG.warn("Organization name is empty for organization with ID: " + agency.getId());
        }

        if (StringUtils.isNotBlank(agency.getStatus())) {
            result.setActive(ACTIVE.equals(agency.getStatus()));
        }

        return result;
    }

    default Service extractService(ICarolProgram program, String providerName) {
        Service result = mapService(program);
        if (StringUtils.isBlank(program.getRequiredDocumentation())) {
            result.setApplicationProcess(program.getApplicationProcess());
        } else {
            result.setApplicationProcess(
                MapperUtils.joinNotBlank(" ", program.getApplicationProcess(),
                    "Required items: " + program.getRequiredDocumentation()));
        }
        result.setProviderName(providerName);
        return result;
    }

    default Set<OpeningHours> extractOpeningHours(ICarolHours hours) {
        if (hours == null || hours.getDays() == null || BooleanUtils.isTrue(hours.getIsConfidential())) {
            return new HashSet<>();
        }
        ICarolDay[] days = hours.getDays();
        return Arrays.stream(days)
            .map(this::mapOpeningHours)
            .collect(Collectors.toSet());
    }

    default Optional<AccessibilityForDisabilities> extractAccessibilityForDisabilities(ICarolSite site) {
        if (site.getAccessibility() == null) {
            return Optional.empty();
        }

        if (StringUtils.isBlank(site.getAccessibility().getDisabled())) {
            LOG.warn("Missing required parameters for accessibility for disabilities for organization with ID: "
                + site.getId());
            return Optional.empty();
        }

        return Optional.of(mapAccessibility(site.getAccessibility()));
    }

    default Set<Phone> extractPhones(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHONE_NUMBER))
            .map(this::mapContactToPhone)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    default Optional<Location> extractLocation(ICarolSite site, String providerName) {
        return Arrays.stream(site.getContactDetails())
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(entry -> mapToLocation(entry, site.getLastVerifiedOn(), site.getId(), providerName));
    }

    default Set<Language> extractLangs(ICarolProgram program) {
        if (BooleanUtils.isTrue(program.getIsConfidential())) {
            return new HashSet<>();
        }
        String[] langs = program.getLanguagesOffered().split(LISTS_DELIMITER);
        return Arrays.stream(langs).map(lang -> new Language().language(lang))
            .collect(Collectors.toSet());
    }

    default Eligibility extractEligibility(ICarolProgram program) {
        if (StringUtils.isBlank(program.getEligibility())) {
            LOG.warn("Missing required eligibility field for organization with ID: " + program.getId());
            return null;
        }

        return mapEligibility(program);
    }

    default Optional<PhysicalAddress> extractPhysicalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(PHYSICAL_LOCATION))
            .findFirst().map(this::mapToPhysicalAddress);
    }

    default Optional<PostalAddress> extractPostalAddress(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(POSTAL_ADDRESS))
            .findFirst().map(this::mapToPostalAddress);
    }

    @Named("weekday")
    default int getIdByTheWeekday(String weekday) {
        return OpeningHoursUtils.getWeekday(weekday);
    }

    @Mapping(target = "number", source = "contact.number")
    @Mapping(target = "type", source = "contact.label")
    @Mapping(target = "description", source = "contact.description")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Phone toPhone(ICarolContactDetails details);

    @Mapping(source = "contact.line1", target = "address1", defaultValue = "N/A")
    @Mapping(source = "contact.zipPostalCode", target = "postalCode", defaultValue = "N/A")
    @Mapping(source = "contact.country", target = "country", defaultValue = "N/A")
    @Mapping(source = "contact.stateProvince", target = "stateProvince", defaultValue = "N/A")
    @Mapping(source = "contact.city", target = "city", defaultValue = "N/A")
    @Mapping(source = "contact.county", target = "region", defaultValue = "N/A")
    @Mapping(target = "id", ignore = true)
    PostalAddress toPostalAddress(ICarolContactDetails details);
}
