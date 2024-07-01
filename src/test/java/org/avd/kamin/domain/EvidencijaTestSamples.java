package org.avd.kamin.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EvidencijaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Evidencija getEvidencijaSample1() {
        return new Evidencija()
            .id(1L)
            .nazivEvidencija("nazivEvidencija1")
            .vrijemeUsluge("vrijemeUsluge1")
            .komentar("komentar1")
            .imeStanara("imeStanara1")
            .prezimeStanara("prezimeStanara1")
            .kontaktStanara("kontaktStanara1")
            .komentarIspravka("komentarIspravka1")
            .kucniBroj("kucniBroj1");
    }

    public static Evidencija getEvidencijaSample2() {
        return new Evidencija()
            .id(2L)
            .nazivEvidencija("nazivEvidencija2")
            .vrijemeUsluge("vrijemeUsluge2")
            .komentar("komentar2")
            .imeStanara("imeStanara2")
            .prezimeStanara("prezimeStanara2")
            .kontaktStanara("kontaktStanara2")
            .komentarIspravka("komentarIspravka2")
            .kucniBroj("kucniBroj2");
    }

    public static Evidencija getEvidencijaRandomSampleGenerator() {
        return new Evidencija()
            .id(longCount.incrementAndGet())
            .nazivEvidencija(UUID.randomUUID().toString())
            .vrijemeUsluge(UUID.randomUUID().toString())
            .komentar(UUID.randomUUID().toString())
            .imeStanara(UUID.randomUUID().toString())
            .prezimeStanara(UUID.randomUUID().toString())
            .kontaktStanara(UUID.randomUUID().toString())
            .komentarIspravka(UUID.randomUUID().toString())
            .kucniBroj(UUID.randomUUID().toString());
    }
}
