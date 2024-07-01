package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GradTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Grad getGradSample1() {
        return new Grad().id(1L).gradNaziv("gradNaziv1");
    }

    public static Grad getGradSample2() {
        return new Grad().id(2L).gradNaziv("gradNaziv2");
    }

    public static Grad getGradRandomSampleGenerator() {
        return new Grad().id(longCount.incrementAndGet()).gradNaziv(UUID.randomUUID().toString());
    }
}
