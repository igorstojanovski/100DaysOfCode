package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
}
