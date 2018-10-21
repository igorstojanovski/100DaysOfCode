package co.igorski.centralcommittee.model.events;

import co.igorski.centralcommittee.model.CcTest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestStarted extends Event {
    private Long runId;
    private CcTest test;
}