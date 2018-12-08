package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {
    List<Result> findByTestId(Long testId);
}
