package org.avd.kamin.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RasporedTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Raspored getRasporedSample1() {
        return new Raspored().id(1L);
    }

    public static Raspored getRasporedSample2() {
        return new Raspored().id(2L);
    }

    public static Raspored getRasporedRandomSampleGenerator() {
        return new Raspored().id(longCount.incrementAndGet());
    }
}
