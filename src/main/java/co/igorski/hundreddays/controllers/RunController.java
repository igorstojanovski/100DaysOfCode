package co.igorski.hundreddays.controllers;

import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.services.RunService;
import co.igorski.hundreddays.stores.RunStore;
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
    private RunStore runStore;

    @GetMapping
    public ResponseEntity<List<Run>> getAllActiveRuns() {
        List<Run> activeRuns = runStore.getActiveRuns();

        return new ResponseEntity<>(activeRuns, HttpStatus.ACCEPTED);
    }
}
