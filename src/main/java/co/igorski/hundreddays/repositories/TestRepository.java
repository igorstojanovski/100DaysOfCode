package co.igorski.hundreddays.repositories;

import co.igorski.hundreddays.model.CcTest;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TestRepository extends PagingAndSortingRepository<CcTest, Long> {

    CcTest findByTestName(String testName);

}
