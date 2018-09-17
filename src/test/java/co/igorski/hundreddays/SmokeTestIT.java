package co.igorski.hundreddays;

import co.igorski.hundreddays.model.Organization;
import co.igorski.hundreddays.model.Run;
import co.igorski.hundreddays.model.User;
import co.igorski.hundreddays.model.events.RunStarted;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmokeTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exampleTest() {
        Organization organization = new Organization();
        organization.setId("5b943fbd6e644024f4cab9e2");

        User user = new User();
        user.setName("Igor");
        user.setUsername("igorski");
        user.setId("5b94464d6e6440221810064c");
        user.setOrganizationId("5b943fbd6e644024f4cab9e2");

        co.igorski.hundreddays.model.Test test = new co.igorski.hundreddays.model.Test();
        test.setTestName("shouldMarkRunAsStarted");
        test.setTestPath("org.igorski");

        RunStarted runStarted = new RunStarted();
        runStarted.setOrganization(organization);
        runStarted.setUser(user);
        runStarted.setTests(Collections.singletonList(test));

        ResponseEntity<Run> runResponseEntity = restTemplate.postForEntity("/event/run/started", runStarted, Run.class);

        assertThat(runResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @TestConfiguration
    static class Config {

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder().setConnectTimeout(1000).setReadTimeout(1000);
        }

    }

}
