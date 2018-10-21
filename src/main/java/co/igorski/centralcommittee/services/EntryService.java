package co.igorski.centralcommittee.services;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Entry;
import co.igorski.centralcommittee.model.Result;
import co.igorski.centralcommittee.model.Status;
import co.igorski.centralcommittee.model.events.RunStarted;
import co.igorski.centralcommittee.repositories.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    private TestService testService;
    @Autowired
    private EntryRepository entryRepository;

    public List<Entry> createEntries(RunStarted runStartedEvent) {
        List<Entry> entries = new ArrayList<>();

        for(CcTest test : runStartedEvent.getTests()) {
            Result result = new Result();
            result.setStatus(Status.QUEUED);

            Entry entry = new Entry();
            entry.setResult(result);
            CcTest orCreate = testService.getOrCreate(test);
            entry.setTest(orCreate);
            entry.setTest(orCreate);

            entries.add(entryRepository.save(entry));
        }
        return entries;
    }

}
