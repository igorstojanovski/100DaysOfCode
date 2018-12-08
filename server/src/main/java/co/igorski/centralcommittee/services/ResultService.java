package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public List<Result> getHistory(Long testId) {
        return resultRepository.findByTestId(testId);
    }
}
