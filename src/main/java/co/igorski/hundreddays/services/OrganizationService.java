package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    private Optional<Organization> getOrganization(String organizationId) {
        return organizationRepository.findById(organizationId);
    }

    Organization validateOrganization(String organizationId) throws DataException {
        return getOrganization(organizationId)
                    .orElseThrow(() -> new DataException("No such organization exists."));
    }
}
