package co.igorski.mongodbspringexample.controllers;

import co.igorski.mongodbspringexample.model.Run;
import co.igorski.mongodbspringexample.services.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/run")
public class RunController {

    @Autowired
    private RunService runService;

    @GetMapping
    public ResponseEntity<List<Run>> getAllActiveRuns() {
        List<Run> activeRuns = runService.getActiveRuns();

        return new ResponseEntity<>(activeRuns, HttpStatus.ACCEPTED);
    }
}
