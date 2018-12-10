package co.igorski.centralcommittee.model.events;

import co.igorski.centralcommittee.model.CcTest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TestReported extends Event {
    private Long runId;
    private CcTest test;
    private Map<String, String> reportEntries;
}
