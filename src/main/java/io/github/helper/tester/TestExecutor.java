package io.github.helper.tester;

import io.github.helper.IoContext;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

/**
 * Orchestra l'esecuzione di una lista di {@link TestCase} contro l'azione fornita
 * dallo studente (tipicamente una method reference al suo metodo da testare).
 * <p>
 * Delega la gestione degli stream a {@link OutputCapture} e la stampa dei risultati
 * a {@link ResultReporter}: questa classe si occupa solo del "flusso di controllo"
 * del ciclo di test (ordinamento, interruzione dopo il primo fallimento segreto,
 * continuazione dopo un fallimento pubblico, ecc.).
 */
public class TestExecutor {

    private final List<TestCase> testCases;
    private final OutputCapture outputCapture;
    private final ResultReporter reporter;

    public TestExecutor(List<TestCase> testCases, OutputCapture outputCapture, ResultReporter reporter) {
        this.testCases = testCases;
        // i test pubblici vengono sempre eseguiti prima di quelli segreti
        this.testCases.sort(Comparator.comparing(TestCase::isSecret));
        this.outputCapture = outputCapture;
        this.reporter = reporter;
    }

    /**
     * Esegue tutti i test contro l'azione fornita.
     *
     * @return true se tutti i test (pubblici e segreti) sono stati superati
     */
    public boolean run(Runnable action) {
        boolean allPassed = true;
        boolean publicFailed = false;
        int publicIndex = 1;
        int secretIndex = 1;

        outputCapture.start();

        try {
            for (TestCase tc : testCases) {
                // Se siamo arrivati ai test segreti ma c'è stato almeno un errore nei pubblici, ci fermiamo qui
                if (tc.isSecret() && publicFailed) {
                    break;
                }

                String testName = tc.isSecret() ? "TEST SEGRETO " + secretIndex++ : "TEST " + publicIndex++;

                outputCapture.redirect(tc.isSecret());
                outputCapture.reset();
                IoContext.setInputStream(new ByteArrayInputStream(tc.getInput().getBytes(StandardCharsets.UTF_8)));

                try {
                    action.run();
                } catch (Exception e) {
                    allPassed = false;
                    String errorMsg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

                    if (tc.isSecret()) {
                        outputCapture.redirect(false);
                        reporter.printCrashBox(testName, tc.getInput(), errorMsg);
                        break; // i test segreti si bloccano subito
                    } else {
                        publicFailed = true;
                        reporter.printCrashBox(testName, tc.getInput(), errorMsg);
                        continue; // i test pubblici continuano fino alla fine dei pubblici
                    }
                }

                String outputProdotto = OutputFormatter.normalize(outputCapture.getCapturedOutput());
                String outputAtteso = OutputFormatter.normalize(tc.getExpectedOutput().trim());

                if (!outputProdotto.equals(outputAtteso)) {
                    allPassed = false;
                    if (tc.isSecret()) {
                        outputCapture.redirect(false);
                        reporter.printFailureBox(testName, tc.getInput(), outputAtteso, outputProdotto);
                        break; // i test segreti si bloccano subito
                    } else {
                        publicFailed = true;
                        reporter.printFailureBox(testName, tc.getInput(), outputAtteso, outputProdotto);
                    }
                } else if (!tc.isSecret()) {
                    reporter.printSuccess(testName);
                }
            }

            outputCapture.redirect(false);
            reporter.printFinalSummary(allPassed);
            return allPassed;

        } catch (Exception e) {
            reporter.printGeneralError(e.getMessage());
            return false;
        } finally {
            outputCapture.restore();
        }
    }
}