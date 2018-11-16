package co.igorski.centralcommittee.controllers.events;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.TestFinished;
import co.igorski.centralcommittee.model.events.TestStarted;
import co.igorski.centralcommittee.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event/test")
public class TestEventController {

    private final TestService testService;

    @Autowired
    public TestEventController(TestService testService) {
        this.testService = testService;
    }

    @Autowired
    private KafkaTemplate<String, Event> template;

    @PostMapping
    @RequestMapping("/started")
    public ResponseEntity<CcTest> runFinishedStarted(@RequestBody TestStarted testStarted) {
        template.send("test-events", testStarted);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping
    @RequestMapping("/finished")
    public ResponseEntity<CcTest> runFinishedHandle(@RequestBody TestFinished testFinished) {
        template.send("test-events", testFinished);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
