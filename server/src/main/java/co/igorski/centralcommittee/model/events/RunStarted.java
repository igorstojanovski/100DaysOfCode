package co.igorski.centralcommittee.model.events;

import co.igorski.centralcommittee.model.CcTest;
import co.igorski.centralcommittee.model.Organization;
import co.igorski.centralcommittee.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RunStarted extends Event {
    private Organization organization;
    private User user;
    private List<CcTest> tests;
    private String projectName;
}
