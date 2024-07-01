package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VrstaUslugeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static VrstaUsluge getVrstaUslugeSample1() {
        return new VrstaUsluge().id(1L).vrstaUslugeNaziv("vrstaUslugeNaziv1");
    }

    public static VrstaUsluge getVrstaUslugeSample2() {
        return new VrstaUsluge().id(2L).vrstaUslugeNaziv("vrstaUslugeNaziv2");
    }

    public static VrstaUsluge getVrstaUslugeRandomSampleGenerator() {
        return new VrstaUsluge().id(longCount.incrementAndGet()).vrstaUslugeNaziv(UUID.randomUUID().toString());
    }
}
