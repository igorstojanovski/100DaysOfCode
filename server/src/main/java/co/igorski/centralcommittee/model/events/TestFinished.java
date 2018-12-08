package co.igorski.centralcommittee.model.events;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Outcome;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestFinished extends Event {
    private Long runId;
    private CcTest test;
    private Outcome outcome;
    private String error;
}
