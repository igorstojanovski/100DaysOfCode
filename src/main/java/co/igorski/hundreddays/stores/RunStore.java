package co.igorski.hundreddays.stores;

import co.igorski.hundreddays.model.Run;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RunStore {
    private final Map<String, Run> activeRuns = new HashMap<>();

    public void activateRun(Run created) {
        activeRuns.put(created.getId(), created);
    }

    public Run deactivateRun(String runId) {
        return activeRuns.remove(runId);
    }

    public List<Run> getActiveRuns() {
        return new ArrayList<>(activeRuns.values());
    }

    public Run getRun(String runId) {
        return activeRuns.get(runId);
    }
}
