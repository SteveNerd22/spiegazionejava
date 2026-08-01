package io.github.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tester {
    private static PrintStream originalOut;
    private static ByteArrayOutputStream capturedStream;
    private static List<TestCase> testCases;
    private static PrintStream consoleOut;
    private static PrintStream silentOut;

    public static void setup() {
        testCases = new ArrayList<>(List.of(
                // --- TEST PUBBLICI (5) ---
                new TestCase("30\n2\n1\n", "10.0", false),
                new TestCase("30\n7\n1\n", "15.0", false),
                new TestCase("15\n3\n4\n", "4.0", false),
                new TestCase("10\n1\n1\n", "5.0", false),
                new TestCase("25\n6\n2\n", "10.0", false),

                // --- TEST SEGRETI (10) ---
                new TestCase("12\n7\n1\n", "7.5", true),
                new TestCase("40\n6\n1\n", "12.0", true),
                new TestCase("40\n6\n4\n", "8.0", true),
                new TestCase("14\n7\n4\n", "5.5", true),
                new TestCase("35\n7\n2\n", "13.0", true),
                new TestCase("16\n3\n5\n", "4.0", true),
                new TestCase("50\n4\n10\n", "8.0", true),
                new TestCase("15\n6\n1\n", "6.0", true),
                new TestCase("30\n7\n5\n", "11.0", true),
                new TestCase("13\n7\n6\n", "6.5", true)
        ));

        testCases.sort(Comparator.comparing(TestCase::isSecret));

        originalOut = System.out;
        capturedStream = new ByteArrayOutputStream();
        consoleOut = originalOut;

        silentOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // Scarta l'output a video ma lo memorizza nel capturedStream
            }
        });

        updateSystemOut(false);
    }

    private static void updateSystemOut(boolean isSecret) {
        OutputStream activeConsole = isSecret ? silentOut : consoleOut;
        TeeOutputStream tee = new TeeOutputStream(activeConsole, capturedStream);
        System.setOut(new PrintStream(tee, true));
    }

    public static void verify(Runnable action) {
        boolean allPassed = true;
        boolean publicFailed = false;
        int publicIndex = 1;
        int secretIndex = 1;

        try {
            for (TestCase tc : testCases) {
                // Se siamo arrivati ai test segreti ma c'è stato almeno un errore nei pubblici, ci fermiamo qui
                if (tc.isSecret() && publicFailed) {
                    break;
                }

                String testName = tc.isSecret() ? "TEST SEGRETO " + secretIndex++ : "TEST " + publicIndex++;

                updateSystemOut(tc.isSecret());

                capturedStream.reset();
                IoContext.setInputStream(new ByteArrayInputStream(tc.getInput().getBytes(StandardCharsets.UTF_8)));

                try {
                    action.run();
                } catch (Exception e) {
                    allPassed = false;
                    String errorMsg = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();

                    if (tc.isSecret()) {
                        updateSystemOut(false);
                        printFailureBox(testName, tc.getInput(), "Eccezione nel codice: " + errorMsg, "CRASH");
                        break; // I test segreti si bloccano subito
                    } else {
                        publicFailed = true;
                        printFailureBox(testName, tc.getInput(), "Eccezione nel codice: " + errorMsg, "CRASH");
                        // I test pubblici continuano fino alla fine dei pubblici
                        continue;
                    }
                }

                String outputProdotto = formatOutput(capturedStream.toString().trim());
                String outputAtteso = formatOutput(tc.getExpectedOutput().trim());

                if (!outputProdotto.equals(outputAtteso)) {
                    allPassed = false;
                    if (tc.isSecret()) {
                        updateSystemOut(false);
                        printFailureBox(testName, tc.getInput(), outputAtteso, outputProdotto);
                        break; // I test segreti si bloccano subito
                    } else {
                        publicFailed = true;
                        printFailureBox(testName, tc.getInput(), outputAtteso, outputProdotto);
                        // I test pubblici continuano fino alla fine dei pubblici
                    }
                } else {
                    if (!tc.isSecret()) {
                        System.out.println("----------------------------------------");
                        System.out.println(" ✔ [" + testName + "] SUPERATO");
                        System.out.println("----------------------------------------");
                    }
                }
            }

            updateSystemOut(false);

            System.out.println("\n========================================");
            if (allPassed) {
                System.out.println(" 🎉 COMPLIMENTI! Tutti i test sono superati!");
            } else {
                System.out.println(" ❌ Test falliti. Correggi il codice e riprova.");
            }
            System.out.println("========================================");

        } catch (Exception e) {
            updateSystemOut(false);
            System.err.println("\n[ERRORE GENERALE NEL TESTER]: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
    }

    private static String formatOutput(String raw) {
        try {
            double val = Double.parseDouble(raw);
            if (val == (long) val) {
                return String.valueOf((long) val) + ".0";
            }
            return String.valueOf(val);
        } catch (NumberFormatException e) {
            return raw;
        }
    }

    private static void printFailureBox(String testName, String input, String atteso, String ricevuto) {
        System.out.println("\n========================================");
        System.out.println(" ❌ [" + testName + "] FALLITO");
        System.out.println("----------------------------------------");
        System.out.println(" • Input inserito : " + input.replace("\n", " | "));
        System.out.println(" • Output atteso  : " + atteso);
        System.out.println(" • Output ricevuto: " + ricevuto);
        System.out.println("========================================");
    }
}