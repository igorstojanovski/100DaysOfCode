package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
}
