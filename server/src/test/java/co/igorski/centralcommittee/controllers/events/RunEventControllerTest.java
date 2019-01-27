package co.igorski.centralcommittee.controllers.events;

import co.igorski.centralcommittee.model.*;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.RunFinished;
import co.igorski.centralcommittee.model.events.RunStarted;
import co.igorski.centralcommittee.model.responses.RunCreatedResponse;
import co.igorski.centralcommittee.services.ProjectService;
import co.igorski.centralcommittee.services.RunService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunEventControllerTest {

    private static final String PROJECT_NAME = "DemoProject";
    private static Organization organization;
    private static User user;
    private static CcTest testOne;
    private static CcTest testTwo;
    @Mock
    private RunService runService;
    @Mock
    private ProjectService projectService;
    private RunEventController runController;
    @Mock
    private Project mockProject;
    @Mock
    private KafkaTemplate<String, Event> template;

    @BeforeAll
    public static void beforeAll() {
        organization = new Organization();
        organization.setId(1234L);

        user = new User();
        user.setName("Igor");
        user.setUsername("igorski");
        user.setId(12222L);
        user.setOrganization(organization);

        testOne = new CcTest();
        testOne.setTestName("shouldMarkRunAsStarted");
        testOne.setTestPath("org.igorski");

        testTwo = new CcTest();
        testTwo.setTestName("shouldMarkRunAsFinished");
        testTwo.setTestPath("org.igorski");
    }

    @BeforeEach
    public void beforeEach() {
        runController = new RunEventController(runService, projectService, template);
    }

    @Test
    public void shouldReturnIdOfCreatedRunWhenStarted() {
        ArrayList<CcTest> tests = new ArrayList<>();
        tests.add(testOne);
        tests.add(testTwo);

        RunStarted runStarted = new RunStarted();
        runStarted.setTests(tests);
        runStarted.setUser(user);
        runStarted.setOrganization(organization);
        runStarted.setTimestamp(new Date());
        runStarted.setProjectName(PROJECT_NAME);

        Run run = new Run();
        run.setId(124L);
        when(runService.startRun(runStarted, mockProject)).thenReturn(run);

        when(projectService.getProject("DemoProject")).thenReturn(mockProject);

        ResponseEntity runResponseEntity = runController.runStarted(runStarted);

        RunCreatedResponse runCreatedResponse = (RunCreatedResponse) runResponseEntity.getBody();
        assertThat(runCreatedResponse.getId()).isEqualTo(124L);
        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldReturnRunObjectWhenFinished() {
        RunFinished runFinished = new RunFinished();
        runFinished.setRunId(1L);

        ResponseEntity<Run> runResponseEntity = runController.runFinished(runFinished);
        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    @Test
    public void shouldThrowWhenTimeStampNotPresent() {
        ArrayList<CcTest> tests = new ArrayList<>();
        tests.add(testOne);

        RunStarted runStarted = new RunStarted();
        runStarted.setTests(tests);
        runStarted.setUser(user);
        runStarted.setOrganization(organization);

        assertThat(runController.runStarted(runStarted).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}