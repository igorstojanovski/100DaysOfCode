package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.*;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.repositories.RunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RunService {

    @Autowired
    private RunRepository runRepository;
    @Autowired
    private ResultService resultService;
    private final Map<String, Run> activeRuns = new HashMap<>();

    public Run startRun(RunStarted runStartedEvent) {

        Run run = new Run();
        run.setOrganizationId(runStartedEvent.getOrganization().getId());
        run.setStart(new Date());
        run.setUserId(runStartedEvent.getUser().getId());
        run.setResults(resultService.addResults(runStartedEvent));

        Run created = runRepository.save(run);
        activeRuns.put(created.getId(), created);

        return created;
    }

    public List<Run> getActiveRuns() {
        return new ArrayList<>(activeRuns.values());
    }
}
