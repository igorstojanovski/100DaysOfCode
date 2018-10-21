package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.repositories.ResultRepository;
import co.igorski.centralcommittee.repositories.RunRepository;
import co.igorski.centralcommittee.stores.RunStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunServiceTest {

    @Mock
    private RunStore runStore;
    @Mock
    private RunRepository runRepository;
    @Mock
    private EntryService entryService;
    @Captor
    ArgumentCaptor<Run> runCaptor;
    private RunService runService;
    @Mock
    private ResultRepository resultRepository;

    @BeforeEach
    public void beforeEach() {
        runService = new RunService(runStore, runRepository, entryService, resultRepository);
    }

    @Test
    public void shouldGetAllRunsFromService() {
        List<Run> runs = new ArrayList<>();
        Run run = new Run();
        runs.add(run);

        when(runRepository.findAll()).thenReturn(runs);
        List<Run> responseRuns = runService.getAllRuns();

        assertThat(responseRuns).hasSize(1);
        assertThat(responseRuns.get(0)).isEqualTo(run);
    }

    @Test
    public void shouldSaveAndActivateRun() {
//        Organization organization = new Organization();
//        organization.setId("O1");
//
//        User user = new User();
//        user.setId("U1");
//
//        CcTest test = new CcTest();
//        test.setId("T1");
//        test.setTestName("shouldRunSimpleExample");
//        test.setTestPath("co.igorski");
//
//        List<CcTest> tests = new ArrayList<>(1);
//        tests.add(test);
//
//        RunStarted runStartedEvent = new RunStarted();
//        runStartedEvent.setOrganization(organization);
//        runStartedEvent.setUser(user);
//        runStartedEvent.setTests(tests);
//        Date currentDate = new Date();
//        runStartedEvent.setTimestamp(currentDate);
//
//        Result result = new Result();
//        List<Result> results = new ArrayList<>(1);
//        results.add(result);
//
//        when(entryService.addResults(runStartedEvent)).thenReturn(results);
//        Run dummyRun = new Run();
//        when(runRepository.save(any(Run.class))).thenReturn(dummyRun);
//        runService.startRun(runStartedEvent);
//
//        verify(runRepository).save(runCaptor.capture());
//
//        Run capturedRun = runCaptor.getValue();
//
//        assertThat(capturedRun.getStartTime()).isEqualTo(currentDate);
//        assertThat(capturedRun.getOrganization()).isEqualTo("O1");
//        assertThat(capturedRun.getUserId()).isEqualTo("U1");
//
//        List<Result> capturedResults = capturedRun.getResults();
//
//        assertThat(capturedResults).hasSize(1);
//        assertThat(capturedResults.get(0)).isEqualTo(result);
//        verify(runStore).activateRun(dummyRun);
    }
}