package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
}
