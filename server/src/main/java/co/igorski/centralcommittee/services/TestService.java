package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.*;
import co.igorski.centralcommittee.model.events.*;
import co.igorski.centralcommittee.repositories.TestRepository;
import co.igorski.centralcommittee.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {
    private static final Logger LOG = LoggerFactory.getLogger(TestService.class);
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

        LOG.info("Received event of type: " + event.getClass().getName());

        if(event instanceof TestStarted) {
            testStarted((TestStarted) event);
        } else if(event instanceof TestFinished) {
            if(testFinished((TestFinished) event)) {
                template.send("test-events", event);
            }
        } else if (event instanceof TestDisabled) {
            setTestDisabled((TestDisabled) event);
        } else if (event instanceof TestReported) {
            testReported((TestReported) event);
        }
    }

    private void setTestDisabled(TestDisabled testDisabled) {
        Run run = runStore.getRun(testDisabled.getRunId());
        Result result = getTestResult(run, testDisabled.getTest());
        result.setStatus(Status.DISABLED);
    }

    /**
     * If the {@link CcTest} object exists it will retrieve it from DB if not it will
     * create one.
     *
     * @param test the test we want to create
     * @return the test object that exists in the database
     */
    CcTest getOrCreate(CcTest test) {
        CcTest testByTestPath = testRepository.findByTestPath(test.getTestPath());
        return Objects.requireNonNullElseGet(testByTestPath, () -> testRepository.save(test));
    }

    private boolean testStarted(TestStarted testStarted) {
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

    private void testReported(TestReported testReported) {
        Run run = runStore.getRun(testReported.getRunId());
        Result result = getTestResult(run, testReported.getTest());

        for(Map.Entry<String, String> reportEntry : testReported.getReportEntries().entrySet()) {
            result.addReportEntry(new ReportEntry(testReported.getTimestamp(), reportEntry.getKey(), reportEntry.getValue()));
        }
    }

    private Result getTestResult(Run run, CcTest test) {
        return run.getResults().get(test.getTestPath());
    }

    private boolean testFinished(TestFinished testFinished) {
        Run run = runStore.getRun(testFinished.getRunId());
        Result result = getTestResult(run, testFinished.getTest());
        boolean markedFinished = false;
        if(result != null && result.getStatus() == Status.RUNNING) {
            result.setStatus(Status.FINISHED);
            result.setOutcome(testFinished.getOutcome());
            result.setEndTime(new Date());
            result.setError(testFinished.getError());
            markedFinished = true;
        }

        return markedFinished;
    }
}
