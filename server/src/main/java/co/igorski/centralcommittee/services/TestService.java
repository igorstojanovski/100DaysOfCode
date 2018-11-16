package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Entry;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.model.Status;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.TestFinished;
import co.igorski.centralcommittee.model.events.TestStarted;
import co.igorski.centralcommittee.repositories.TestRepository;
import co.igorski.centralcommittee.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
            result.setStartTime(new Date());
            markedStarted = true;
        }

        return markedStarted;
    }

    private Result getTestResult(Run run, CcTest test) {

        List<Entry> entries = run.getEntries();
        Result result = null;

        for(Entry entry : entries) {
            if(entry.getTest().equals(test)) {
                result = entry.getResult();
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
            result.setEndTime(new Date());
            markedFinished = true;
        }

        return markedFinished;
    }

    public CcTest getTest(Long testId) {
        return testRepository.findById(testId).get();
    }

    public Page<CcTest> getAllTests(Pageable pageable) {
        return testRepository.findAll(pageable);
    }

    public Object countAll() {
        return testRepository.findAll();
    }
}
