package co.igorski.hundreddays.services;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Entry;
import co.igorski.hundreddays.model.Result;
import co.igorski.hundreddays.model.Status;
import co.igorski.hundreddays.model.events.RunStarted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    private TestService testService;

    public List<Entry> createEntries(RunStarted runStartedEvent) {
        List<Entry> entries = new ArrayList<>();

        for(CcTest test : runStartedEvent.getTests()) {
            Result result = new Result();
            result.setStatus(Status.QUEUED);

            Entry entry = new Entry();
            entry.setResult(result);
            CcTest orCreate = testService.getOrCreate(test);
            entry.setTestId(orCreate.getId());
            entry.setTest(orCreate);

            entries.add(entry);
        }
        return entries;
    }

}
