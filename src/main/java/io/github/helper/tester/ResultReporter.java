package io.github.helper.tester;

import java.util.List;

/**
 * Responsabile della sola formattazione/stampa dei messaggi verso lo studente:
 * esito dei singoli test, box di fallimento, riepilogo finale.
 * Non contiene alcuna logica di esecuzione o confronto: riceve solo i dati da mostrare.
 */
public class ResultReporter {

    public void printSuccess(String testName) {
        System.out.println("----------------------------------------");
        System.out.println(" \u2714 [" + testName + "] SUPERATO");
        System.out.println("----------------------------------------");
    }

    public void printStructuralCheckHeader() {
        System.out.println("\n========================================");
        System.out.println(" VERIFICA VINCOLI STRUTTURALI");
        System.out.println("========================================");
    }

    public void printStructuralCheckResult(String description, StructuralCheckResult result) {
        String icon = result.passed() ? "[ok]" : "\u274C";
        System.out.println(" " + icon + " " + description);
    }

    public void printStructuralChecksFailedSummary(List<StructuralCheckResult> failedResults) {
        System.out.println("\n========================================");
        System.out.println(" \u274C Uno o più vincoli strutturali non sono rispettati.");

        for (StructuralCheckResult result : failedResults) {
            System.out.println();
            for (String line : result.message().split("\n")) {
                System.out.println(" " + line);
            }
        }

        System.out.println("\n Correggi il codice: i test funzionali partiranno solo dopo.");
        System.out.println("========================================");
    }

    public void printFailureBox(String testName, String input, String atteso, String ricevuto) {
        System.out.println("\n========================================");
        System.out.println(" \u274C [" + testName + "] FALLITO");
        System.out.println("----------------------------------------");
        System.out.println(" \u2022 Input inserito : " + input.replace("\n", " | "));
        System.out.println(" \u2022 Output atteso  : " + atteso);
        System.out.println(" \u2022 Output ricevuto: " + ricevuto);
        System.out.println("========================================");
    }

    public void printCrashBox(String testName, String input, String errorMessage) {
        printFailureBox(testName, input, "Eccezione nel codice: " + errorMessage, "CRASH");
    }

    public void printFinalSummary(boolean allPassed) {
        System.out.println("\n========================================");
        if (allPassed) {
            System.out.println(" \uD83C\uDF89 COMPLIMENTI! Tutti i test sono superati!");
        } else {
            System.out.println(" \u274C Test falliti. Correggi il codice e riprova.");
        }
        System.out.println("========================================");
    }

    public void printGeneralError(String message) {
        System.err.println("\n[ERRORE GENERALE NEL TESTER]: " + message);
    }
}