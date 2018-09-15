package co.igorski.mongodbspringexample.repositories;

import co.igorski.mongodbspringexample.model.Run;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends MongoRepository<Run, String> {
}
