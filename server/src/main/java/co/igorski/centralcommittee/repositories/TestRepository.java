package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TestRepository extends PagingAndSortingRepository<CcTest, Long> {

    CcTest findByTestName(String testName);

    CcTest findByTestPath(String testPath);

    Page<CcTest> findByProject(Project project, Pageable pageable);

    Object countByProject(Project project);
}
