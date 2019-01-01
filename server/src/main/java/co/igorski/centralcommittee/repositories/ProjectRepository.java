package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends PagingAndSortingRepository<Project, Long> {
    Project findByName(String name);
}