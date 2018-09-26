package co.igorski.hundreddays.model.events;

import co.igorski.hundreddays.model.CcTest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TestStarted extends Event {
    private String runId;
    private CcTest test;
}