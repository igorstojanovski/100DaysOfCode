package co.igorski.mongodbspringexample.services;

import co.igorski.mongodbspringexample.model.Result;
import co.igorski.mongodbspringexample.model.Status;
import co.igorski.mongodbspringexample.model.Test;
import co.igorski.mongodbspringexample.model.events.RunStarted;
import co.igorski.mongodbspringexample.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private TestService testService;

    public List<Result> addResults(RunStarted runStartedEvent) {
        List<Result> results = new ArrayList<>();

        for(Test test : runStartedEvent.getTests()) {
            Result result = new Result();
            result.setStatus(Status.QUEUED);
            result.setTest(testService.getOrCreate(test));
            resultRepository.save(result);
            results.add(result);
        }
        return results;
    }
}
