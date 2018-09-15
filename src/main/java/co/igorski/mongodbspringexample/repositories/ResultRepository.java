package co.igorski.mongodbspringexample.repositories;

import co.igorski.mongodbspringexample.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends MongoRepository<Result, String> {
}
