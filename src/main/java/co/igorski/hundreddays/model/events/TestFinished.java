package co.igorski.hundreddays.model.events;

import co.igorski.hundreddays.model.CcTest;
import co.igorski.hundreddays.model.Outcome;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestFinished extends Event {
    private String runId;
    private CcTest test;
    private Outcome outcome;
}
