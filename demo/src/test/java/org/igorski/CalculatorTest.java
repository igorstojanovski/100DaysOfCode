package org.igorski;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Igor Stojanovski.
 * Date: 11/13/2018
 * Time: 10:05 PM
 */
class CalculatorTest {

    @Test
    public void shouldAddNumbers(TestReporter testReporter) {
        testReporter.publishEntry("Starting test");
        Calculator calculator = new Calculator();
        assertThat(calculator.add(new int[] {1,2,3,4,5})).isEqualTo(15);
        testReporter.publishEntry("Ending test");
    }

    @Disabled
    @Test
    public void disabledTest(TestReporter testReporter) {
        testReporter.publishEntry("This is actually a disabled test and this should not be logged.");
    }

}