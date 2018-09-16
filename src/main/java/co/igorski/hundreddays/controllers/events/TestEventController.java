package co.igorski.hundreddays.controllers.events;

import co.igorski.hundreddays.model.Test;
import co.igorski.hundreddays.model.events.TestFinished;
import co.igorski.hundreddays.model.events.TestStarted;
import co.igorski.hundreddays.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event/test")
public class TestEventController {

    @Autowired
    private TestService testService;

    @PostMapping
    @RequestMapping("/started")
    public ResponseEntity<Test> runFinishedStarted(@RequestBody TestStarted testStarted) {
        boolean markedAsStarted = testService.testStarted(testStarted);
        return new ResponseEntity<>(markedAsStarted ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping
    @RequestMapping("/finished")
    public ResponseEntity<Test> runFinishedHandle(@RequestBody TestFinished testFinished) {
        boolean markedAsFinished = testService.testFinished(testFinished);
        return new ResponseEntity<>(markedAsFinished ? HttpStatus.ACCEPTED : HttpStatus.NOT_ACCEPTABLE);
    }
}
