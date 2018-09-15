package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Run;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RunRepository extends MongoRepository<Run, String> {
}
