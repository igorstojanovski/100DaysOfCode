package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.Entry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends CrudRepository<Entry, Long> {
}
