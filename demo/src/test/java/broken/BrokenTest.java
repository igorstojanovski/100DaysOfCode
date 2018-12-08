package broken;

import org.junit.jupiter.api.Test;

public class BrokenTest {


    @Test
    public void shouldFail() throws Exception {
        throw new Exception("Test failing intentionally.");
    }
}
