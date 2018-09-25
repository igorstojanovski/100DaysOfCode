package co.igorski.hundreddays.stores;

import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.web.DataListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RunStore {
    private final Map<String, Run> activeRuns = new HashMap<>();
    private final List<DataListener> listeners = new ArrayList<>();

    public void activateRun(Run created) {
        activeRuns.put(created.getId(), created);
        notifyAllListeners();
    }

    public Run deactivateRun(String runId) {
        Run run = activeRuns.remove(runId);
        notifyAllListeners();
        return run;
    }

    public List<Run> getActiveRuns() {
        return new ArrayList<>(activeRuns.values());
    }

    public Collection<Run> getLiveRuns() {
        return activeRuns.values();
    }

    public Run getRun(String runId) {
        return activeRuns.get(runId);
    }

    public void registerListener(DataListener dataListener) {
        listeners.add(dataListener);
    }

    private void notifyAllListeners() {
        for(DataListener dataListener : listeners) {
            dataListener.dataChanged();
        }
    }
}
