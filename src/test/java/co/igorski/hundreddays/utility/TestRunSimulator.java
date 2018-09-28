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

    @BeforeAll
    public static void beforeAll() {
        restTemplate = new TestRestTemplate();
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
        CcTest testOne = new CcTest();
        testOne.setTestName("shouldRunTestOne");
        testOne.setTestPath("org.igorski");

        CcTest testTwo = new CcTest();
        testTwo.setTestName("shouldRunTestTwo");
        testTwo.setTestPath("org.igorski");

        CcTest testThree = new CcTest();
        testThree.setTestName("shouldRunTestThree");
        testThree.setTestPath("org.igorski");

        CcTest testFour = new CcTest();
        testFour.setTestName("shouldRunTestFour");
        testFour.setTestPath("org.igorski");

        List<CcTest> tests = new ArrayList<>();
        tests.add(testOne);
        tests.add(testTwo);
        tests.add(testThree);
        tests.add(testFour);
        // ------------------------------------------

        RunStarted runStarted = new RunStarted();
        runStarted.setOrganization(organization);
        runStarted.setUser(user);
        runStarted.setTests(tests);
        runStarted.setTimestamp(new Date());

        ResponseEntity<Run> runResponseEntity = restTemplate.postForEntity(RUN_STARTED, runStarted, Run.class);
        String runId = runResponseEntity.getBody().getId();

        // ----------- START TESTS -----------------

        TestStarted testOneStarted = new TestStarted();
        testOneStarted.setRunId(runId);
        testOneStarted.setTimestamp(new Date());
        testOneStarted.setTest(testOne);

        TestStarted testTwoStarted = new TestStarted();
        testTwoStarted.setRunId(runId);
        testTwoStarted.setTimestamp(new Date());
        testTwoStarted.setTest(testTwo);

        TestStarted testThreeStarted = new TestStarted();
        testThreeStarted.setRunId(runId);
        testThreeStarted.setTimestamp(new Date());
        testThreeStarted.setTest(testThree);

        TestStarted testFourStarted = new TestStarted();
        testFourStarted.setRunId(runId);
        testFourStarted.setTimestamp(new Date());
        testFourStarted.setTest(testFour);

        restTemplate.postForEntity(TEST_STARTED, testOneStarted, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_STARTED, testTwoStarted, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_STARTED, testThreeStarted, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_STARTED, testFourStarted, CcTest.class);
        // ------------------------------------------

        // ----------- FINISH TESTS -----------------
        TestFinished testOneFinished = new TestFinished();
        testOneFinished.setRunId(runId);
        testOneFinished.setTimestamp(new Date());
        testOneFinished.setTest(testOne);
        testOneFinished.setOutcome(toggleOutcome());

        TestFinished testTwoFinished = new TestFinished();
        testTwoFinished.setRunId(runId);
        testTwoFinished.setTimestamp(new Date());
        testTwoFinished.setTest(testTwo);
        testTwoFinished.setOutcome(toggleOutcome());

        TestFinished testThreeFinished = new TestFinished();
        testThreeFinished.setRunId(runId);
        testThreeFinished.setTimestamp(new Date());
        testThreeFinished.setTest(testThree);
        testThreeFinished.setOutcome(toggleOutcome());

        TestFinished testFourFinished = new TestFinished();
        testFourFinished.setRunId(runId);
        testFourFinished.setTimestamp(new Date());
        testFourFinished.setTest(testFour);
        testFourFinished.setOutcome(toggleOutcome());

        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_FINISHED, testOneFinished, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_FINISHED, testTwoFinished, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_FINISHED, testFourFinished, CcTest.class);
        Thread.sleep(getMillis());
        restTemplate.postForEntity(TEST_FINISHED, testThreeFinished, CcTest.class);

        // ------------------------------------------

        RunFinished runFinished = new RunFinished();
        runFinished.setRunId(runId);

        Thread.sleep(getMillis());
        restTemplate.postForEntity(RUN_FINISHED, runFinished, Run.class);
    }

    private long getMillis() {
        return RANDOM.nextInt(1000);
    }

    private Outcome toggleOutcome() {
        return RANDOM.nextInt(100)/2 == 1 ? Outcome.PASSED : Outcome.FAILED;
    }
}
