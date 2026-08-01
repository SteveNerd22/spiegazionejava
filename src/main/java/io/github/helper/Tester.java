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

    public static void setupPart2() {
        List<TestCase> cases = new ArrayList<>(List.of(
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

        setup(cases);
    }

    public static void setupPart3() {
        List<TestCase> cases = new ArrayList<>(List.of(
                // --- TEST PUBBLICI (5) ---
                // 1. Cuscino: 5, Stress: 1 (Tranquillo), Cappelli: 2 -> 10 + (5*1.5) + (2*3) = 10 + 7.5 + 6 = 23.5
                new TestCase("5\n1\n2\n", "23.5", false),

                // 2. Cuscino: 10, Stress: 2 (Stressato), Cappelli: 0 -> 10 + (10*1.5) + 12 = 10 + 15 + 12 = 37.0
                new TestCase("10\n2\n0\n", "37.0", false),

                // 3. Cuscino: 2, Stress: 3 (Crisi), Cappelli: 5 -> 10 + (2*1.5) + 25 + (5*3) = 10 + 3 + 25 + 15 = 53.0
                new TestCase("2\n3\n5\n", "53.0", false),

                // 4. Cuscino: 0, Stress: 1, Cappelli: 0 -> 10.0
                new TestCase("0\n1\n0\n", "10.0", false),

                // 5. Cuscino: 50, Stress: 3, Cappelli: 7 -> 10 + (50*1.5) + 25 + (7*3) = 10 + 75 + 25 + 21 = 131.0 -> max 100.0
                new TestCase("50\n3\n7\n", "100.0", false),

                // --- TEST SEGRETI (10) ---
                // 6. Cuscino: 4, Stress: 2, Cappelli: 3 -> 10 + 6 + 12 + 9 = 37.0
                new TestCase("4\n2\n3\n", "37.0", true),

                // 7. Cuscino: 15, Stress: 1, Cappelli: 1 -> 10 + 22.5 + 3 = 35.5
                new TestCase("15\n1\n1\n", "35.5", true),

                // 8. Cuscino: 8, Stress: 3, Cappelli: 4 -> 10 + 12 + 25 + 12 = 59.0
                new TestCase("8\n3\n4\n", "59.0", true),

                // 9. Cuscino: -7, Stress: 2, Cappelli: 6 -> 10 + 12 + 18 = 40.0
                new TestCase("-7\n2\n6\n", "40.0", true),

                // 10. Cuscino: 20, Stress: 3, Cappelli: 0 -> 10 + 30 + 25 = 65.0
                new TestCase("20\n3\n0\n", "65.0", true),

                // 11. Cuscino: 1, Stress: 1, Cappelli: 0 -> 10 + 1.5 = 11.5
                new TestCase("1\n1\n0\n", "11.5", true),

                // 12. Cuscino: 30, Stress: 2, Cappelli: 5 -> 10 + 45 + 12 + 15 = 82.0
                new TestCase("30\n2\n5\n", "82.0", true),

                // 13. Cuscino: 6, Stress: 3, Cappelli: 2 -> 10 + 9 + 25 + 6 = 50.0
                new TestCase("6\n3\n2\n", "50.0", true),

                // 14. Cuscino: 12, Stress: 2, Cappelli: 4 -> 10 + 18 + 12 + 12 = 52.0
                new TestCase("12\n2\n4\n", "52.0", true),

                // 15. Cuscino: 60, Stress: 3, Cappelli: 7 -> Supera 100 -> 100.0
                new TestCase("60\n3\n7\n", "100.0", true)
        ));

        setup(cases);
    }

    public static void setup(List<TestCase> cases) {
        testCases = cases;

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