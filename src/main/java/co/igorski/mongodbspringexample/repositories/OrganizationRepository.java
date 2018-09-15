package co.igorski.mongodbspringexample.repositories;

import co.igorski.mongodbspringexample.model.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {
}
