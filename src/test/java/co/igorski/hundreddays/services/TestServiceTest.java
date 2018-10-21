package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.model.Outcome;
import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.Status;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import co.igorski.hundreddays.repositories.TestRepository;
import co.igorski.hundreddays.stores.RunStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class TestServiceTest {

    @Mock
    private TestRepository testRepository;
    @Mock
    private RunStore runStore;
    private CcTest theTest;
    private Run run;

    @BeforeEach
    public void beforeEach() {
        theTest = new CcTest();
        theTest.setTestName("shouldMarkRunAsStarted");
        theTest.setTestPath("org.igorski");

        List<Result> results = new ArrayList<>();
        Result result = new Result();
        result.setStatus(Status.RUNNING);
        results.add(result);

        run = new Run();
        run.setId(1L);
        run.setStartTime(new Date());
        Organization organization = new Organization();
        organization.setId(1L);
        run.setOrganization(organization);
    }

    @Test
    public void shouldUpdateStartedTestInRunObject() {
        TestService testService = new TestService(testRepository, runStore);
        TestStarted testStarted = new TestStarted();
        testStarted.setTest(theTest);
        testStarted.setTimestamp(new Date());
        testStarted.setRunId(1L);

        when(runStore.getRun(1L)).thenReturn(run);

        testService.testStarted(testStarted);

        assertThat(run.getEntries().get(0).getResult().getStatus()).isEqualTo(Status.RUNNING);
        assertThat(run.getEntries().get(0).getResult().getStartTime()).isNotNull();
    }

    @Test
    public void shouldUpdateFinishedTestInRunObject() {
        TestService testService = new TestService(testRepository, runStore);
        TestFinished testFinished = new TestFinished();
        testFinished.setTest(theTest);
        testFinished.setTimestamp(new Date());
        testFinished.setRunId(1L);
        testFinished.setOutcome(Outcome.PASSED);

        when(runStore.getRun(1L)).thenReturn(run);

        testService.testFinished(testFinished);

        assertThat(run.getEntries().get(0).getResult().getStatus()).isEqualTo(Status.FINISHED);
        assertThat(run.getEntries().get(0).getResult().getOutcome()).isEqualTo(Outcome.PASSED);
        assertThat(run.getEntries().get(0).getResult().getEndTime()).isNotNull();
    }

    @Test
    public void shouldNotCreateTestWhenOneExists() {
        when(testRepository.findByTestName("shouldMarkRunAsStarted")).thenReturn(theTest);
        TestService testService = new TestService(testRepository, runStore);
        testService.getOrCreate(theTest);

        verify(testRepository, times(0)).save(any());
    }

    @Test
    public void shouldCreateTestWhenNoneExists() {
        when(testRepository.findByTestName("shouldMarkRunAsStarted")).thenReturn(null);
        TestService testService = new TestService(testRepository, runStore);
        testService.getOrCreate(theTest);

        verify(testRepository, times(1)).save(theTest);
    }
}