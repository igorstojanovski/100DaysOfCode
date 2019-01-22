package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.Group;
import co.igorski.centralcommittee.model.Project;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {
    List<Group> findByProject(Project project);

    Object countByProject(Project project);
}
