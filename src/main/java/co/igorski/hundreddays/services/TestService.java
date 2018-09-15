package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.Test;
import co.igorski.hundreddays.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    /**
     * If the {@link Test} object exists it will retrieve it from DB if not it will
     * create one.
     *
     * @param test the test we want to create
     * @return the test object that exists in the database
     */
    public Test getOrCreate(Test test) {
        Test byTestName = testRepository.findByTestName(test.getTestName());
        if(byTestName != null) {
            return byTestName;
        } else {
            return testRepository.save(test);
        }
    }

}
