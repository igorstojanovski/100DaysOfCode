package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.CcTest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<CcTest, String> {

    CcTest findByTestName(String testName);

}
