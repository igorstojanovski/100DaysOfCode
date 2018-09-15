package co.igorski.mongodbspringexample.services;

import co.igorski.mongodbspringexample.model.*;
import co.igorski.mongodbspringexample.model.events.RunStarted;
import co.igorski.mongodbspringexample.repositories.RunRepository;
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
