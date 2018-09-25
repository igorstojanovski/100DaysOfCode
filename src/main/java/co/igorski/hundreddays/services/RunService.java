package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.*;
import co.igorski.hundreddays.model.events.Event;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.repositories.RunRepository;
import co.igorski.hundreddays.stores.RunStore;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RunService {

    @Autowired
    private RunStore runStore;
    @Autowired
    private RunRepository runRepository;
    @Autowired
    private ResultService resultService;
    @Autowired
    private OrganizationService organizationService;

    @KafkaListener(topics = "test-events", groupId = "run")
    public void eventHandler(ConsumerRecord<String, Event> cr) {
        System.out.println("Run Service received event: " + cr.value().getClass());
        Event event = cr.value();
        if(event instanceof RunFinished) {
            endRun((RunFinished) event);
        }
    }

    public Run startRun(RunStarted runStartedEvent) {

        Run run = new Run();
        run.setOrganizationId(runStartedEvent.getOrganization().getId());
        run.setStart(new Date());
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
}
