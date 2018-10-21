package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
}
