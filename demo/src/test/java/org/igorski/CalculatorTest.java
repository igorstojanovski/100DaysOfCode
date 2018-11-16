package org.igorski;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Igor Stojanovski.
 * Date: 11/13/2018
 * Time: 10:05 PM
 */
class CalculatorTest {

    @Test
    public void shouldAddNumbers() {
        Calculator calculator = new Calculator();
        assertThat(calculator.add(new int[] {1,2,3,4,5})).isEqualTo(15);
    }

    @Disabled
    @Test
    public void disabledTest() {

    }

}