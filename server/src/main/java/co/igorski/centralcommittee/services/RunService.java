package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.Entry;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RunService {
    private final ResultRepository resultRepository;
    private RunStore runStore;
    private RunRepository runRepository;
    private EntryService entryService;

    @Autowired
    public RunService(RunStore runStore, RunRepository runRepository, EntryService entryService,
                      ResultRepository resultRepository) {
        this.runStore = runStore;
        this.runRepository = runRepository;
        this.entryService = entryService;
        this.resultRepository = resultRepository;
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
        run.setOrganization(runStartedEvent.getOrganization());
        run.setStartTime(runStartedEvent.getTimestamp());
        run.setUser(runStartedEvent.getUser());
        run.setEntries(entryService.createEntries(runStartedEvent));

        Run created = runRepository.save(run);
        runStore.activateRun(created);

        return created;
    }

    public Run endRun(RunFinished runFinished) {
        Run run = runStore.deactivateRun(runFinished.getRunId());
        run.setEndTime(new Date());

        for (Entry entry : run.getEntries()) {
            resultRepository.save(entry.getResult());
        }

        runRepository.save(run);

        return run;
    }

    public List<Run> getAllRuns() {
        List<Run> target = new ArrayList<>();
        Iterable<Run> all = runRepository.findAll();
        all.iterator().forEachRemaining(target::add);
        return target;
    }

    public List<Entry> getEntries(Long runId) {
        Optional<Run> runOptional = runRepository.findById(runId);
        List<Entry> entries = new ArrayList<>();
        if(runOptional.isPresent()) {
            entries = runOptional.get().getEntries();
        }
        return entries;
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

    public Collection<Run> getParticipatingRuns(Long testId) {
        return runRepository.findParticipatingRuns(testId);
    }
}
