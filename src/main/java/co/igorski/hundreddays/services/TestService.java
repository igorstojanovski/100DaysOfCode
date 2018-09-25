package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.Status;
import co.igorski.hundreddays.model.events.Event;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import co.igorski.hundreddays.repositories.TestRepository;
import co.igorski.hundreddays.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Service
public class TestService {

    private TestRepository testRepository;
    private RunStore runStore;

    @Autowired
    private KafkaTemplate<String, Event> template;

    @Autowired
    public TestService(TestRepository testRepository, RunStore runStore) {
        this.testRepository = testRepository;
        this.runStore = runStore;
    }

    @KafkaListener(topics = "test-events", groupId = "test")
    public void eventListener(ConsumerRecord<String, Event> cr) {
        Event event = cr.value();
        System.out.println("TEST Service received event: " + event.getClass());
        if(event instanceof TestStarted) {
            testStarted((TestStarted) event);
        } else if(event instanceof TestFinished) {
            if(testFinished((TestFinished) event)) {
                template.send("test-events", event);
            }
        }
    }


    /**
     * If the {@link CcTest} object exists it will retrieve it from DB if not it will
     * create one.
     *
     * @param test the test we want to create
     * @return the test object that exists in the database
     */
    CcTest getOrCreate(CcTest test) {
        CcTest byTestName = testRepository.findByTestName(test.getTestName());
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
            result.setStart(new Date());
            markedStarted = true;
        }

        return markedStarted;
    }

    private Result getTestResult(Run run, CcTest test) {
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
        if(result != null && result.getStatus() == Status.RUNNING) {
            result.setStatus(Status.FINISHED);
            result.setOutcome(testFinished.getOutcome());
            result.setEnd(new Date());
            markedFinished = true;
        }

        return markedFinished;
    }
}
