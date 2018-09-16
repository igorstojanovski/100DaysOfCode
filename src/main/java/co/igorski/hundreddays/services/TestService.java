package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.*;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import co.igorski.hundreddays.repositories.TestRepository;
import co.igorski.hundreddays.stores.RunStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private RunStore runStore;

    /**
     * If the {@link Test} object exists it will retrieve it from DB if not it will
     * create one.
     *
     * @param test the test we want to create
     * @return the test object that exists in the database
     */
    Test getOrCreate(Test test) {
        Test byTestName = testRepository.findByTestName(test.getTestName());
        if(byTestName != null) {
            return byTestName;
        } else {
            return testRepository.save(test);
        }
    }

    public boolean testStarted(TestStarted testStarted) {
        Run run = runStore.getRun(testStarted.getRunId());
        Result result = getTestResult(run, testStarted.getTest());
        boolean markedStarted = false;
        if(result != null) {
            result.setStatus(Status.RUNNING);
            markedStarted = true;
        }

        return markedStarted;
    }

    private Result getTestResult(Run run, Test test) {
        Collection<Result> results = run.getResults();
        Result result = null;

        for(Result testResult : results) {
            if(testResult.getTest().equals(test)) {
                result = testResult;
                break;
            }
        }

        return result;
    }

    public boolean testFinished(TestFinished testFinished) {
        Run run = runStore.getRun(testFinished.getRunId());
        Result result = getTestResult(run, testFinished.getTest());
        boolean markedFinished = false;
        if(result != null) {
            result.setStatus(Status.RUNNING);
            result.setOutcome(testFinished.getOutcome());
            markedFinished = true;
        }

        return markedFinished;
    }
}
