package co.igorski.mongodbspringexample.repositories;

import co.igorski.mongodbspringexample.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
