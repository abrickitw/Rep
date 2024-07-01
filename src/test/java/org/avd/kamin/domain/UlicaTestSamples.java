package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UlicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ulica getUlicaSample1() {
        return new Ulica().id(1L).ulicaNaziv("ulicaNaziv1");
    }

    public static Ulica getUlicaSample2() {
        return new Ulica().id(2L).ulicaNaziv("ulicaNaziv2");
    }

    public static Ulica getUlicaRandomSampleGenerator() {
        return new Ulica().id(longCount.incrementAndGet()).ulicaNaziv(UUID.randomUUID().toString());
    }
}
