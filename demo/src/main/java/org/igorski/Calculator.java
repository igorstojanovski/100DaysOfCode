package org.igorski;

import java.util.Arrays;

/**
 * Created by Igor Stojanovski.
 * Date: 11/13/2018
 * Time: 10:02 PM
 */
public class Calculator {

    public int add(int[] numbers) {
        return Arrays.stream(numbers).sum();
    }

}
