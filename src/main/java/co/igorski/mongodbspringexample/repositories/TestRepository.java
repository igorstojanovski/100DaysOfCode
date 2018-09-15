package co.igorski.mongodbspringexample.repositories;

import co.igorski.mongodbspringexample.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestRepository extends MongoRepository<Test, String> {

    Test findByTestName(String testName);

}
