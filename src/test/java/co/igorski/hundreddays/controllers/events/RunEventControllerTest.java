package co.igorski.hundreddays.controllers.events;

import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.services.RunService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class RunEventControllerTest {

    private static Organization organization;
    private static User user;
    private static co.igorski.hundreddays.model.Test testOne;
    private static co.igorski.hundreddays.model.Test testTwo;
    @Mock
    private RunService runService;
    private RunEventController runController;

    @BeforeAll
    public static void beforeAll() {
        organization = new Organization();
        organization.setId("5b943fbd6e644024f4cab9e2");

        user = new User();
        user.setName("Igor");
        user.setUsername("igorski");
        user.setId("5b94464d6e6440221810064c");
        user.setOrganizationId("5b943fbd6e644024f4cab9e2");

        testOne = new co.igorski.hundreddays.model.Test();
        testOne.setTestName("shouldMarkRunAsStarted");
        testOne.setTestPath("org.igorski");

        testTwo = new co.igorski.hundreddays.model.Test();
        testTwo.setTestName("shouldMarkRunAsFinished");
        testTwo.setTestPath("org.igorski");
    }

    @BeforeEach
    public void beforeEach() {
        runController = new RunEventController(runService);
    }

    @Test
    public void shouldReturnRunObjectWhenStarted() {
        ArrayList<co.igorski.hundreddays.model.Test> tests = new ArrayList<>();
        tests.add(testOne);
        tests.add(testTwo);

        RunStarted runStarted = new RunStarted();
        runStarted.setTests(tests);
        runStarted.setUser(user);
        runStarted.setOrganization(organization);

        Run run = new Run();
        when(runService.startRun(runStarted)).thenReturn(run);

        ResponseEntity<Run> runResponseEntity = runController.runStarted(runStarted);

        assertThat(runResponseEntity.getBody()).isEqualTo(run);
        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldReturnRunObjectWhenFinished() {
        RunFinished runFinished = new RunFinished();
        runFinished.setRunId("1");

        Run run = new Run();
        when(runService.endRun(runFinished)).thenReturn(run);

        ResponseEntity<Run> runResponseEntity = runController.runFinished(runFinished);

        assertThat(runResponseEntity.getBody()).isEqualTo(run);
        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }
}