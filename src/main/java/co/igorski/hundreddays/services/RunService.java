package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.events.Event;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.repositories.RunRepository;
import co.igorski.hundreddays.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RunService {

    private RunStore runStore;
    private RunRepository runRepository;
    private ResultService resultService;

    @Autowired
    public RunService(RunStore runStore, RunRepository runRepository, ResultService resultService) {
        this.runStore = runStore;
        this.runRepository = runRepository;
        this.resultService = resultService;
    }

    @KafkaListener(topics = "test-events", groupId = "run")
    public void eventHandler(ConsumerRecord<String, Event> cr) {
        Event event = cr.value();
        if(event instanceof RunFinished) {
            endRun((RunFinished) event);
        }
    }

    public Run startRun(RunStarted runStartedEvent) {

        Run run = new Run();
        run.setOrganizationId(runStartedEvent.getOrganization().getId());
        run.setStart(runStartedEvent.getTimestamp());
        run.setUserId(runStartedEvent.getUser().getId());
        run.setResults(resultService.addResults(runStartedEvent));

        Run created = runRepository.save(run);
        runStore.activateRun(created);

        return created;
    }

    public Run endRun(RunFinished runFinished) {
        Run run = runStore.deactivateRun(runFinished.getRunId());
        run.setEnd(new Date());
        runRepository.save(run);

        return run;
    }

    public List<Run> getAllRuns() {
        return runRepository.findAll();
    }

    public Page<Run> getRuns(int offset, int limit) {
        System.out.println("OFFSET : " + offset + " LIMIT: " + limit);
        return runRepository.findAll(PageRequest.of(offset, offset + limit));
    }

    public int getRunCount() {
        return (int) runRepository.findAll().size();
    }

    public List<Result> getRunResults(String runId) {
        Optional<Run> runOptional = runRepository.findById(runId);
        List<Result> results = new ArrayList<>();
        if(runOptional.isPresent()) {
            results = runOptional.get().getResults();
        }
        return results;
    }

    public String getFormattedTestDuration(Result result) {
        LocalDateTime start = result.getStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = result.getEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();

        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
