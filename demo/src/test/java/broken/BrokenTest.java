package broken;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

public class BrokenTest {


    @Test
    public void shouldFail(TestReporter reporter) throws Exception {
        reporter.publishEntry("Running broken test.");
        throw new Exception("Test failing intentionally.");
    }
}
