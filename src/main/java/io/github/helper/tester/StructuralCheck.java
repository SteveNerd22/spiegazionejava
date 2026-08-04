package io.github.helper.tester;

/**
 * Rappresenta un vincolo strutturale sul codice sorgente dello studente
 * (es. "massimo N print", "nessun ciclo", "nessuna ricorsione"...).
 * <p>
 * L'implementazione concreta di COSA cercare vive tipicamente come factory statica
 * dentro {@link TestCase} (es. {@link TestCase#maxPrintCalls}); {@link StructuralAnalyzer}
 * fornisce invece solo le primitive generiche per navigare il sorgente (motore),
 * senza sapere nulla del dominio specifico dei singoli esercizi.
 */
public interface StructuralCheck {

    /** Descrizione leggibile del vincolo, mostrata nel report indipendentemente dall'esito. */
    String description();

    /** Valuta il vincolo usando l'analyzer fornito, e ne ritorna l'esito. */
    StructuralCheckResult evaluate(StructuralAnalyzer analyzer);
}