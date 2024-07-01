package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NaseljeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Naselje getNaseljeSample1() {
        return new Naselje().id(1L).naseljeNaziv("naseljeNaziv1");
    }

    public static Naselje getNaseljeSample2() {
        return new Naselje().id(2L).naseljeNaziv("naseljeNaziv2");
    }

    public static Naselje getNaseljeRandomSampleGenerator() {
        return new Naselje().id(longCount.incrementAndGet()).naseljeNaziv(UUID.randomUUID().toString());
    }
}
