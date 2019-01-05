package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.RunFinished;
import co.igorski.centralcommittee.model.events.RunStarted;
import co.igorski.centralcommittee.repositories.ResultRepository;
import co.igorski.centralcommittee.repositories.RunRepository;
import co.igorski.centralcommittee.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RunService {
    private final ResultRepository resultRepository;
    private final TestService testService;
    private RunStore runStore;
    private RunRepository runRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public RunService(RunStore runStore, RunRepository runRepository, ResultRepository resultRepository,
                      TestService testService) {
        this.runStore = runStore;
        this.runRepository = runRepository;
        this.resultRepository = resultRepository;
        this.testService = testService;
    }

    @KafkaListener(topics = "test-events", groupId = "run")
    public void eventHandler(ConsumerRecord<String, Event> cr) {
        Event event = cr.value();
        if(event instanceof RunFinished) {
            endRun((RunFinished) event);
        }
    }

    public Run startRun(RunStarted runStartedEvent, Project project) {

        Run run = new Run();
        run.setProject(project);
        run.setOrganization(runStartedEvent.getOrganization());
        run.setStartTime(runStartedEvent.getTimestamp());
        run.setUser(userService.getUser(runStartedEvent.getUser().getUsername()));

        Map<@NotNull String, Result> testMap = runStartedEvent.getTests().stream()
                .peek(ccTest -> ccTest.setProject(project))
                .collect(Collectors.toMap(CcTest::getTestPath, test -> new Result(testService.getOrCreate(test), run)));
        run.setResults(testMap);

        Run created = runRepository.save(run);
        runStore.activateRun(created);

        return created;
    }

    public Run endRun(RunFinished runFinished) {
        Run run = runStore.deactivateRun(runFinished.getRunId());
        run.setEndTime(new Date());

        runRepository.save(run);

        return run;
    }

    public List<Run> getAllRuns() {
        List<Run> target = new ArrayList<>();
        Iterable<Run> all = runRepository.findAll();
        all.iterator().forEachRemaining(target::add);
        return target;
    }

    public String getFormattedTestDuration(Result result) {
        LocalDateTime start = getLocalDateTime(result.getStartTime());
        LocalDateTime end = getLocalDateTime(result.getEndTime());

        if(start == null || end == null) {
            return "";
        }

        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private LocalDateTime getLocalDateTime(Date time) {
        LocalDateTime localDateTime = null;
        if(time != null) {
            localDateTime = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return localDateTime;
    }

    public Run getRun(Long runId) {
        return runRepository.findById(runId).get();
    }
}
