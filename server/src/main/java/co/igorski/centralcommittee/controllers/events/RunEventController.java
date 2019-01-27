package co.igorski.centralcommittee.controllers.events;

import co.igorski.centralcommittee.model.Project;
import co.igorski.centralcommittee.model.Run;
import co.igorski.centralcommittee.model.events.Event;
import co.igorski.centralcommittee.model.events.RunFinished;
import co.igorski.centralcommittee.model.events.RunStarted;
import co.igorski.centralcommittee.model.responses.RunCreatedResponse;
import co.igorski.centralcommittee.services.ProjectService;
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

    private final ProjectService projectService;
    private RunService runService;

    @Autowired
    private KafkaTemplate<String, Event> template;

    @Autowired
    public RunEventController(RunService runService, ProjectService projectService, KafkaTemplate<String, Event> template) {
        this.runService = runService;
        this.projectService = projectService;
        this.template = template;
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

        Project project = projectService.getProject(runStartedEvent.getProjectName());
        if (project == null) {
            return new ResponseEntity<>("Project with that name does not exist.", HttpStatus.NOT_FOUND);
        }

        Run run = runService.startRun(runStartedEvent, project);
        RunCreatedResponse runCreatedResponse = new RunCreatedResponse();
        runCreatedResponse.setId(run.getId());
        return new ResponseEntity<>(runCreatedResponse, HttpStatus.CREATED);
    }

    @PostMapping
    @RequestMapping("/finished")
    public ResponseEntity<Run> runFinished(@RequestBody RunFinished runFinished) {
        template.send("test-events", runFinished);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
