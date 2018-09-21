package co.igorski.hundreddays.model.events;

import co.igorski.hundreddays.model.Outcome;
import co.igorski.hundreddays.model.Test;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestFinished extends Event {
    private String runId;
    private Test test;
    private Outcome outcome;
}
