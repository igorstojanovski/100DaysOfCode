package co.igorski.mongodbspringexample.model.events;

import co.igorski.mongodbspringexample.model.Organization;
import co.igorski.mongodbspringexample.model.Test;
import co.igorski.mongodbspringexample.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RunStarted {
    private Organization organization;
    private User user;
    private List<Test> tests;
}
