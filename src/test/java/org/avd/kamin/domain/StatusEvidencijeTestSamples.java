package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StatusEvidencijeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StatusEvidencije getStatusEvidencijeSample1() {
        return new StatusEvidencije().id(1L).statusEvidencijeNaziv("statusEvidencijeNaziv1");
    }

    public static StatusEvidencije getStatusEvidencijeSample2() {
        return new StatusEvidencije().id(2L).statusEvidencijeNaziv("statusEvidencijeNaziv2");
    }

    public static StatusEvidencije getStatusEvidencijeRandomSampleGenerator() {
        return new StatusEvidencije().id(longCount.incrementAndGet()).statusEvidencijeNaziv(UUID.randomUUID().toString());
    }
}
