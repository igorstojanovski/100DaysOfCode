package co.igorski.centralcommittee.controllers.events;

import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.RunFinished;
import co.igorski.centralcommittee.model.events.RunStarted;
import co.igorski.centralcommittee.services.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event/run")
public class RunEventController {

    private RunService runService;

    @Autowired
    private KafkaTemplate<String, Event> template;

    @Autowired
    public RunEventController(RunService runService) {
        this.runService = runService;
    }

    @PostMapping
    @RequestMapping("/started")
    public ResponseEntity runStarted(@RequestBody RunStarted runStartedEvent) {
        if(runStartedEvent.getTimestamp() == null) {
            return new ResponseEntity<>("Timestamp must not be empty.", HttpStatus.BAD_REQUEST);
        }
        if(runStartedEvent.getTests().size() == 0) {
            return new ResponseEntity<>("Tests must be present.", HttpStatus.BAD_REQUEST);
        }
        Run run = runService.startRun(runStartedEvent);
        return new ResponseEntity<>(run, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/finished")
    public ResponseEntity<Run> runFinished(@RequestBody RunFinished runFinished) {
        template.send("test-events", runFinished);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
