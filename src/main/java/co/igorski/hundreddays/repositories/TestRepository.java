package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<Test, String> {

    Test findByTestName(String testName);

}
