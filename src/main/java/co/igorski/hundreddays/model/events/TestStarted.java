package co.igorski.hundreddays.model.events;

import co.igorski.hundreddays.model.Test;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TestStarted {
    private String runId;
    private Test test;
    private Date timestamp;
}
