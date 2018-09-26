package co.igorski.hundreddays;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.model.Outcome;
import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.Status;
import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldSimulateWholeRun() {
        Organization organization = new Organization();
        organization.setId("5b943fbd6e644024f4cab9e2");

        User user = new User();
        user.setName("Igor");
        user.setUsername("igorski");
        user.setId("5b94464d6e6440221810064c");
        user.setOrganizationId("5b943fbd6e644024f4cab9e2");

        CcTest testOne = new CcTest();
        testOne.setTestName("shouldMarkRunAsStarted");
        testOne.setTestPath("org.igorski");

        CcTest testTwo = new CcTest();
        testTwo.setTestName("shouldMarkRunAsFinished");
        testTwo.setTestPath("org.igorski");

        List<CcTest> tests = new ArrayList<>();
        tests.add(testOne);
        tests.add(testTwo);

        RunStarted runStarted = new RunStarted();
        runStarted.setOrganization(organization);
        runStarted.setUser(user);
        runStarted.setTests(tests);

        ResponseEntity<Run> runResponseEntity = restTemplate.postForEntity("/event/run/started", runStarted, Run.class);
        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String runId = runResponseEntity.getBody().getId();

        TestStarted testOneStarted = new TestStarted();
        testOneStarted.setRunId(runId);
        testOneStarted.setTimestamp(new Date());
        testOneStarted.setTest(testOne);

        TestStarted testTwoStarted = new TestStarted();
        testTwoStarted.setRunId(runId);
        testTwoStarted.setTimestamp(new Date());
        testTwoStarted.setTest(testTwo);

        restTemplate.postForEntity("/event/test/started", testOneStarted, CcTest.class);
        restTemplate.postForEntity("/event/test/started", testTwoStarted, CcTest.class);

        TestFinished testOneFinished = new TestFinished();
        testOneFinished.setRunId(runId);
        testOneFinished.setTimestamp(new Date());
        testOneFinished.setTest(testOne);
        testOneFinished.setOutcome(Outcome.FAILED);

        TestFinished testTwoFinished = new TestFinished();
        testTwoFinished.setRunId(runId);
        testTwoFinished.setTimestamp(new Date());
        testTwoFinished.setTest(testTwo);
        testTwoFinished.setOutcome(Outcome.PASSED);

        restTemplate.postForEntity("/event/test/finished", testTwoFinished, CcTest.class);
        restTemplate.postForEntity("/event/test/finished", testOneFinished, CcTest.class);

        await().atMost(5, SECONDS).until(() -> getCurrentResults().get(0).getStatus() == Status.FINISHED);
        await().atMost(5, SECONDS).until(() -> getCurrentResults().get(1).getStatus() == Status.FINISHED);

        RunFinished runFinished = new RunFinished();
        runFinished.setRunId(runId);
        await().atMost(5, SECONDS).until(() -> Objects.requireNonNull(getCurrentRun(runFinished).getBody()).length == 0);
    }

    private ResponseEntity<Run[]> getCurrentRun(RunFinished runFinished) {
        restTemplate.postForEntity("/event/run/finished", runFinished, Run.class);

        return restTemplate.getForEntity("/run", Run[].class);
    }

    private List<Result> getCurrentResults() {
        ResponseEntity<Run[]> activeRunsResponse = restTemplate.getForEntity("/run", Run[].class);
        assertThat(activeRunsResponse.getBody()).hasSize(1);
        Run activeRun = activeRunsResponse.getBody()[0];
        List<Result> currentResults = activeRun.getResults();
        assertThat(currentResults).hasSize(2);
        return currentResults;
    }
}
