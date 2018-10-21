package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Run;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<Run, Long> {
    Page<Run> findByEndTimeNotNull(Pageable pageable);

    Object countByStartTimeNotNull();

    @Query(value = "SELECT r.* FROM Run r, run_entries re, Entry e WHERE r.id = re.run_id AND re.entries_id = e.id AND e.test_id = :testId", nativeQuery = true)
    List<Run> findParticipatingRuns(@Param("testId") Long testId);
}
