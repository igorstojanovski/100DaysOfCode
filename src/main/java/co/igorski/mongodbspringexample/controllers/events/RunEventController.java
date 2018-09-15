package co.igorski.mongodbspringexample.controllers.events;

import co.igorski.mongodbspringexample.model.Run;
import co.igorski.mongodbspringexample.model.events.RunStarted;
import co.igorski.mongodbspringexample.services.RunService;
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

    @Autowired
    private RunService runService;

    @PostMapping
    @RequestMapping("/started")
    public ResponseEntity<Run> startRun(@RequestBody RunStarted runStartedEvent) {
        Run run = runService.startRun(runStartedEvent);
        return new ResponseEntity<>(run, HttpStatus.CREATED);
    }
}
