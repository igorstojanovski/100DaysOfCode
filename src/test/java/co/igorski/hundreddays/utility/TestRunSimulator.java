package co.igorski.hundreddays.utility;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.model.Outcome;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestRunSimulator {
    private static final Random RANDOM = new Random();
    private static final String HOME = "http://localhost:8080";
    private static final String RUN_FINISHED = HOME + "/event/run/finished";
    private static final String TEST_FINISHED = HOME + "/event/test/finished";
    private static final String TEST_STARTED = HOME + "/event/test/started";
    private static final String RUN_STARTED = HOME + "/event/run/started";
    @Autowired
    private static TestRestTemplate restTemplate;
    private static List<String> testNames = new ArrayList<>();
    private List<CcTest> tests = new ArrayList<>();
    private List<TestStarted> testStartedEvents = new ArrayList<>();
    private List<TestFinished> testFinishedEvents = new ArrayList<>();

    @BeforeAll
    public static void beforeAll() {
        restTemplate = new TestRestTemplate();
        testNames.add("shouldRunTestOne");
        testNames.add("shouldRunTestTwo");
        testNames.add("shouldRunTestThree");
        testNames.add("shouldRunTestFour");
        testNames.add("shouldRunTestFive");
        testNames.add("shouldRunTestSix");
        testNames.add("shouldRunTestSeven");
        testNames.add("shouldRunTestEight");
        testNames.add("shouldRunTestNine");
        testNames.add("shouldRunTestTen");
    }

    @BeforeEach
    public void beforeEach() {
        tests.clear();
        testFinishedEvents.clear();
        testStartedEvents.clear();
    }
    @Test
    public void shouldSimulateWholeRun() throws InterruptedException {
        Organization organization = new Organization();
        organization.setId("5b943fbd6e644024f4cab9e2");

        User user = new User();
        user.setName("Igor");
        user.setUsername("igorski");
        user.setId("5b94464d6e6440221810064c");
        user.setOrganizationId("5b943fbd6e644024f4cab9e2");

        // ----------- CREATE TESTS -----------------
        generateTests(RANDOM.nextInt(9) + 1);
        // ------------------------------------------

        RunStarted runStarted = new RunStarted();
        runStarted.setOrganization(organization);
        runStarted.setUser(user);
        runStarted.setTests(tests);
        runStarted.setTimestamp(new Date());

        ResponseEntity<Run> runResponseEntity = restTemplate.postForEntity(RUN_STARTED, runStarted, Run.class);
        String runId = runResponseEntity.getBody().getId();

        // ----------- START TESTS -----------------

        generateTestStartedEvents(runId);
        fireTestStartedEvents();

        // ------------------------------------------

        // ----------- FINISH TESTS -----------------
        generateTestFinishedEvents(runId);
        fireTestFinishedEvents();

        // ------------------------------------------

        RunFinished runFinished = new RunFinished();
        runFinished.setRunId(runId);

        Thread.sleep(getMillis());
        restTemplate.postForEntity(RUN_FINISHED, runFinished, Run.class);
    }

    private void fireTestFinishedEvents() throws InterruptedException {
        for(TestFinished testFinished : testFinishedEvents) {
            Thread.sleep(getMillis());
            restTemplate.postForEntity(TEST_FINISHED, testFinished, CcTest.class);
        }
    }

    private void fireTestStartedEvents() throws InterruptedException {
        for(TestStarted testStarted : testStartedEvents) {
            restTemplate.postForEntity(TEST_STARTED, testStarted, CcTest.class);
            Thread.sleep(getMillis());
        }
    }

    private void generateTests(int count) {
        for(int i = 0 ; i<count; i++) {
            CcTest test = new CcTest();
            test.setTestName(testNames.get(i));
            test.setTestPath("org.igorski");

            tests.add(test);
        }
    }

    private void generateTestStartedEvents(String runId) {
        for(CcTest test : tests) {
            TestStarted testStarted = new TestStarted();
            testStarted.setRunId(runId);
            testStarted.setTimestamp(new Date());
            testStarted.setTest(test);

            testStartedEvents.add(testStarted);
        }
    }

    private void generateTestFinishedEvents(String runId) {
        for(CcTest test : tests) {
            TestFinished testFinished = new TestFinished();
            testFinished.setRunId(runId);
            testFinished.setTimestamp(new Date());
            testFinished.setTest(test);
            testFinished.setOutcome(toggleOutcome());

            testFinishedEvents.add(testFinished);
        }
    }

    private long getMillis() {
        return RANDOM.nextInt(1000);
    }

    private Outcome toggleOutcome() {
        return RANDOM.nextInt(100)%2 == 1 ? Outcome.PASSED : Outcome.FAILED;
    }
}
