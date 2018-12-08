package org.igorski.dummyOne;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class ClassThree {

    private static final Random r = new Random();

    @Test
    public void shouldTestOne() throws InterruptedException {
        System.out.println("Test One - STARTING");
        int millis = r.nextInt(6000);
        Thread.sleep(millis);
        System.out.println("Test One - ENDING");
    }

    @Test
    public void shouldTestTwo() throws Exception {
        System.out.println("Test Two - STARTING");
        int millis = r.nextInt(6000);
        Thread.sleep(millis);
        if (millis < 1500) throw new Exception("Too short wait time.");
        System.out.println("Test Two - ENDING");
    }
    @Test
    public void shouldTestThree() throws Exception {
        System.out.println("Test Three - STARTING");
        int millis = r.nextInt(6000);
        Thread.sleep(millis);
        if (millis < 1500) throw new Exception("Too short wait time.");
        System.out.println("Test Three - ENDING");
    }
    @Test
    public void shouldTestFour() throws Exception {
        System.out.println("Test Four - STARTING");
        int millis = r.nextInt(6000);
        Thread.sleep(millis);
        if (millis < 1500) throw new Exception("Too short wait time.");
        System.out.println("Test Four - ENDING");
    }

    @Test
    public void shouldTestFive() throws Exception {
        System.out.println("Test Five - STARTING");
        int millis = r.nextInt(6000);
        Thread.sleep(millis);
        if (millis < 1500) throw new Exception("Too short wait time.");
        System.out.println("Test Five - ENDING");
    }
}
