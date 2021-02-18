package org.benetech.servicenet.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.service.ServiceBasedImportService;
import org.benetech.servicenet.service.ServiceImportService;
import org.benetech.servicenet.service.ServiceService;
import org.benetech.servicenet.service.ServiceTaxonomyService;
import org.benetech.servicenet.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImportServiceImpl implements ServiceImportService {

    @Autowired
    private EntityManager em;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceBasedImportService serviceBasedImportService;

    @Autowired
    private ServiceTaxonomyService serviceTaxonomyService;

    @Override
    public Service createOrUpdateService(Service filledService, String externalDbId, String providerName,
                                         DataImportReport report) {
        EntityValidator.validateAndFix(filledService, filledService.getOrganization(), report, externalDbId);

        Service service = new Service(filledService);
        Optional<Service> serviceFromDb = serviceService.findWithEagerAssociations(externalDbId, providerName);
        List<String> taxonomiesToKeep = filledService.getTaxonomies().stream()
            .map(t -> t.getTaxonomy().getTaxonomyId())
            .collect(Collectors.toList());
        if (serviceFromDb.isPresent()) {
            if (serviceFromDb.get().deepEquals(filledService)) {
                return serviceFromDb.get();
            }
            deleteOldTaxonomies(serviceFromDb.get(), taxonomiesToKeep);
            fillDataFromDb(service, serviceFromDb.get());
            em.merge(service);
            report.incrementNumberOfUpdatedServices();
        } else {
            em.persist(service);
            report.incrementNumberOfCreatedServices();
        }

        serviceBasedImportService.createOrUpdateEligibility(filledService.getEligibility(), service, report);
        serviceBasedImportService.createOrUpdateLangsForService(filledService.getLangs(), service, report);
        serviceBasedImportService.createOrUpdatePhonesForService(filledService.getPhones(), service, report);
        serviceBasedImportService.createOrUpdateFundingForService(filledService.getFunding(), service, report);
        serviceBasedImportService.createOrUpdateRegularScheduleForService(
            filledService.getRegularSchedule(), service, report);
        serviceBasedImportService.createOrUpdateServiceTaxonomy(
            filledService.getTaxonomies(), providerName, service, report);
        serviceBasedImportService.createOrUpdateRequiredDocuments(filledService.getDocs(), providerName, service, report);
        serviceBasedImportService.createOrUpdateContactsForService(filledService.getContacts(), service, report);
        serviceBasedImportService.createOrUpdateHolidaySchedulesForService(
            filledService.getHolidaySchedules(), service, report);
        serviceBasedImportService.createOrUpdateServiceAtLocationsForService(
            filledService.getLocations(), providerName, service, report);
        serviceBasedImportService.createOrUpdateMetadataForService(filledService.getMetadata(), service, report);

        return service;
    }

    private void deleteOldTaxonomies(Service serviceFromDb, List<String> taxonomiesToKeep) {
        serviceFromDb.getTaxonomies().stream()
            .filter(serviceTaxonomy -> !taxonomiesToKeep.contains(serviceTaxonomy.getTaxonomy().getTaxonomyId()))
            .map(AbstractEntity::getId)
            .forEach(uuid -> serviceTaxonomyService.delete(uuid));
    }

    private void fillDataFromDb(Service newService, Service serviceFromDb) {
        newService.setPhones(serviceFromDb.getPhones());
        newService.setEligibility(serviceFromDb.getEligibility());
        newService.setId(serviceFromDb.getId());
        newService.setRegularSchedule(serviceFromDb.getRegularSchedule());
        newService.setFunding(serviceFromDb.getFunding());
        newService.setHolidaySchedules(serviceFromDb.getHolidaySchedules());
        newService.setContacts(serviceFromDb.getContacts());
        newService.setLangs(serviceFromDb.getLangs());
        newService.setMetadata(serviceFromDb.getMetadata());
    }
}
