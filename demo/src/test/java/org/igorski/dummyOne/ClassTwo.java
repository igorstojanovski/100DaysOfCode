package org.igorski.dummyOne;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class ClassTwo {

    private static final Random r = new Random();

    @Test
    public void shouldTestOne() throws InterruptedException {
        System.out.println("Test One - STARTING");
        Thread.sleep(r.nextInt(3000));
        System.out.println("Test One - ENDING");
    }

    @Test
    public void shouldTestTwo() throws InterruptedException {
        System.out.println("Test Two - STARTING");
        Thread.sleep(r.nextInt(3000));
        System.out.println("Test Two - ENDING");
    }
    @Test
    public void shouldTestThree() throws InterruptedException {
        System.out.println("Test Three - STARTING");
        Thread.sleep(r.nextInt(3000));
        System.out.println("Test Three - ENDING");
    }
    @Test
    public void shouldTestFour() throws InterruptedException {
        System.out.println("Test Four - STARTING");
        Thread.sleep(r.nextInt(3000));
        System.out.println("Test Four - ENDING");
    }

    @Test
    public void shouldTestFive() throws InterruptedException {
        System.out.println("Test Five - STARTING");
        Thread.sleep(r.nextInt(3000));
        System.out.println("Test Five - ENDING");
    }
}
