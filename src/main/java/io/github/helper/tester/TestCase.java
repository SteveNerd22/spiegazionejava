package io.github.helper.tester;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un singolo caso di test funzionale: un input da fornire al programma,
 * l'output che ci si aspetta in risposta, e se si tratti di un test "pubblico"
 * (visibile allo studente in caso di fallimento) o "segreto" (nascosto).
 */
public class TestCase {

    private final String input;
    private final String expectedOutput;
    private final boolean secret;

    public TestCase(String input, String expectedOutput, boolean secret) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.secret = secret;
    }

    /**
     * Crea un TEST PUBBLICO a partire dai valori "grezzi" di input, senza dover
     * costruire a mano la stringa con i separatori "\n".
     * <p>
     * Esempio: {@code TestCase.pub("10.0", 30, 2, 1)} equivale a
     * {@code new TestCase("30\n2\n1\n", "10.0", false)}.
     */
    public static TestCase pub(String expectedOutput, Object... inputValues) {
        return new TestCase(joinInputs(inputValues), expectedOutput, false);
    }

    /** Come {@link #pub}, ma crea un TEST SEGRETO. */
    public static TestCase secret(String expectedOutput, Object... inputValues) {
        return new TestCase(joinInputs(inputValues), expectedOutput, true);
    }

    private static String joinInputs(Object... values) {
        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            sb.append(value).append('\n');
        }
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder fluente, alternativo a {@link #pub} / {@link #secret}, utile quando si
     * preferisce uno stile più esplicito passo-per-passo (es. test più complessi, o
     * quando in futuro si vorranno aggiungere altri attributi opzionali).
     * <p>
     * Esempio: {@code TestCase.builder().input(30, 2, 1).expected("10.0").build()}
     */
    public static class Builder {
        private String input = "";
        private String expectedOutput;
        private boolean secret = false;

        public Builder input(Object... values) {
            this.input = joinInputs(values);
            return this;
        }

        public Builder expected(String expectedOutput) {
            this.expectedOutput = expectedOutput;
            return this;
        }

        /** Marca il test come segreto (default: pubblico). */
        public Builder secret() {
            this.secret = true;
            return this;
        }

        /** Marca esplicitamente il test come pubblico (è già il default). */
        public Builder pub() {
            this.secret = false;
            return this;
        }

        public TestCase build() {
            if (expectedOutput == null) {
                throw new IllegalStateException("Manca expected(...): impossibile costruire il TestCase");
            }
            return new TestCase(input, expectedOutput, secret);
        }
    }

    public String getInput() {
        return input;
    }

    // ---------------------------------------------------------------------
    // Vincoli strutturali (StructuralCheck) — l'implementazione concreta vive
    // qui, come namespace naturale dei controlli sui TestCase/esercizi.
    // Lo StructuralAnalyzer resta "cieco" al dominio: sa solo navigare l'AST.
    // ---------------------------------------------------------------------

    /** Vincola il numero massimo di chiamate a System.out.print/println nel metodo indicato. */
    public static StructuralCheck maxPrintCalls(String methodName, int max) {
        return new StructuralCheck() {
            @Override
            public String description() {
                return "massimo " + max + " chiamata/e a System.out.print/printf/println in '" + methodName + "'";
            }

            @Override
            public StructuralCheckResult evaluate(StructuralAnalyzer analyzer) {
                List<MethodCallExpr> calls = analyzer.findMethodCalls(methodName, call -> {
                    String name = call.getNameAsString();
                    boolean isPrintMethod = name.equals("println") || name.equals("print") || name.equals("printf");
                    boolean isSystemOutScope = call.getScope().map(s -> s.toString().equals("System.out")).orElse(false);
                    return isPrintMethod && isSystemOutScope;
                });

                boolean passed = calls.size() <= max;
                String message = passed
                        ? "Trovate " + calls.size() + " chiamata/e a print/println (massimo consentito: " + max + ")"
                        : buildCallViolationMessage(
                        "Questo esercizio prevede un massimo di " + max + " print, trovati più print del normale in '"
                                + methodName + "':",
                        calls);
                return new StructuralCheckResult(passed, message);
            }
        };
    }

    /** Vincola il numero massimo di chiamate a un metodo generico (per nome) nel metodo indicato. */
    public static StructuralCheck maxCallsTo(String methodName, String calledMethodName, int max) {
        return new StructuralCheck() {
            @Override
            public String description() {
                return "massimo " + max + " chiamata/e a '" + calledMethodName + "' in '" + methodName + "'";
            }

            @Override
            public StructuralCheckResult evaluate(StructuralAnalyzer analyzer) {
                List<MethodCallExpr> calls = analyzer.findMethodCalls(methodName,
                        call -> call.getNameAsString().equals(calledMethodName));

                boolean passed = calls.size() <= max;
                String message = passed
                        ? "Trovate " + calls.size() + " chiamata/e a '" + calledMethodName
                        + "' (massimo consentito: " + max + ")"
                        : buildCallViolationMessage(
                        "Questo esercizio prevede un massimo di " + max + " chiamate a '" + calledMethodName
                                + "', trovate più chiamate del normale in '" + methodName + "':",
                        calls);
                return new StructuralCheckResult(passed, message);
            }
        };
    }

    /** Vieta l'uso di cicli (for, for-each, while, do-while) nel metodo indicato. */
    public static StructuralCheck forbidLoops(String methodName) {
        return new StructuralCheck() {
            @Override
            public String description() {
                return "nessun ciclo (for/while/do-while) consentito in '" + methodName + "'";
            }

            @Override
            public StructuralCheckResult evaluate(StructuralAnalyzer analyzer) {
                List<Node> loops = new ArrayList<>();
                loops.addAll(analyzer.findInMethod(methodName, ForStmt.class));
                loops.addAll(analyzer.findInMethod(methodName, ForEachStmt.class));
                loops.addAll(analyzer.findInMethod(methodName, WhileStmt.class));
                loops.addAll(analyzer.findInMethod(methodName, DoStmt.class));

                boolean passed = loops.isEmpty();
                String message = passed
                        ? "Nessun ciclo trovato"
                        : buildNodeViolationMessage(
                        "Questo esercizio non prevede l'uso di cicli, trovati cicli non consentiti in '"
                                + methodName + "':",
                        loops);
                return new StructuralCheckResult(passed, message);
            }
        };
    }

    /** Riga "System.out.println(\"ciao\"); :: riga 4" per una singola chiamata a metodo. */
    private static String describeCall(MethodCallExpr call) {
        int line = call.getBegin().map(p -> p.line).orElse(-1);
        return call + "; :: riga " + line;
    }

    /** Riga con uno snippet compatto (ridotto se troppo lungo) del nodo + numero di riga. */
    private static String describeNode(Node node) {
        int line = node.getBegin().map(p -> p.line).orElse(-1);
        String snippet = node.toString().replaceAll("\\s+", " ").trim();
        if (snippet.length() > 60) {
            snippet = snippet.substring(0, 60) + "...";
        }
        return snippet + " :: riga " + line;
    }

    private static String buildCallViolationMessage(String header, List<MethodCallExpr> calls) {
        StringBuilder sb = new StringBuilder(header);
        for (MethodCallExpr call : calls) {
            sb.append("\n").append(describeCall(call));
        }
        return sb.toString();
    }

    private static String buildNodeViolationMessage(String header, List<? extends Node> nodes) {
        StringBuilder sb = new StringBuilder(header);
        for (Node node : nodes) {
            sb.append("\n").append(describeNode(node));
        }
        return sb.toString();
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public boolean isSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "input='" + input.replace("\n", "|") + '\'' +
                ", expectedOutput='" + expectedOutput + '\'' +
                ", secret=" + secret +
                '}';
    }
}