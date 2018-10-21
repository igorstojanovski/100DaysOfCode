package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Entry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends CrudRepository<Entry, Long> {
}
