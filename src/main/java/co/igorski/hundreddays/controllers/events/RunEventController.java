package co.igorski.hundreddays.controllers.events;

import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.events.RunFinished;
import co.igorski.hundreddays.model.events.RunStarted;
import co.igorski.hundreddays.services.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event/run")
public class RunEventController {

    private RunService runService;

    @Autowired
    public RunEventController(RunService runService) {
        this.runService = runService;
    }

    @PostMapping
    @RequestMapping("/started")
    public ResponseEntity<Run> runStarted(@RequestBody RunStarted runStartedEvent) {
        Run run = runService.startRun(runStartedEvent);
        return new ResponseEntity<>(run, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/finished")
    public ResponseEntity<Run> runFinished(@RequestBody RunFinished runFinished) {
        Run run = runService.endRun(runFinished);
        return new ResponseEntity<>(run, HttpStatus.ACCEPTED);
    }
}
