package co.igorski.centralcommittee.repositories;

import co.igorski.centralcommittee.model.CcTest;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TestRepository extends PagingAndSortingRepository<CcTest, Long> {

    CcTest findByTestName(String testName);

    CcTest findByTestPath(String testPath);
}
