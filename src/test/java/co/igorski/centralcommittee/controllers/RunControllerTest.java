package co.igorski.centralcommittee.controllers;

import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.services.RunService;
import co.igorski.centralcommittee.stores.RunStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RunControllerTest {

    @Mock
    private RunStore runStore;
    @Mock
    private RunService runService;
    private RunController runController;
    private Run run;
    private List<Run> runs;

    @BeforeEach
    public void beforeEach() {
        runController = new RunController(runStore, runService);
        runs = new ArrayList<>();
        run = new Run();
        runs.add(run);
    }

    @Test
    public void shouldReturnAllRunsFromService() {
        when(runService.getAllRuns()).thenReturn(runs);
        ResponseEntity<List<Run>> runsResponse = runController.getAllRuns();
        assertThat(runsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(run).isEqualTo(runsResponse.getBody().get(0));
    }

    @Test
    public void shouldReturnActiveRunsFromLocalStore() {
        when(runStore.getActiveRuns()).thenReturn(runs);
        ResponseEntity<List<Run>> runsResponse = runController.getAllActiveRuns();
        assertThat(runsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(run).isEqualTo(runsResponse.getBody().get(0));
    }
}