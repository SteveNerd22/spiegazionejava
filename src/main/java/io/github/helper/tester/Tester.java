package io.github.helper.tester;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Punto d'accesso pubblico del motore di test.
 * <p>
 * Un'istanza di {@code Tester} rappresenta una singola "sessione" di verifica per un
 * esercizio specifico: ha i suoi test case, opzionalmente dei vincoli strutturali
 * ({@link StructuralCheck}) e fa riferimento alla classe/sorgente dello studente.
 * <p>
 * Se sono presenti vincoli strutturali, questi vengono valutati UNA SOLA VOLTA, prima
 * dei test funzionali: se anche uno solo fallisce, i test funzionali non vengono eseguiti
 * (non ha senso verificare l'input/output di un codice che viola i vincoli richiesti).
 * <p>
 * Esempio d'uso:
 * <pre>{@code
 * Tester tester = Tester.builder()
 *         .targetClass(Part2.class)
 *         .testCases(TestCaseRepository.part2())
 *         .structuralCheck(TestCase.maxPrintCalls("part2", 1))
 *         .build();
 *
 * tester.verify(Part2::part2);
 * }</pre>
 */
public class Tester {

    private final TestExecutor executor;
    private final ResultReporter reporter;
    private final List<StructuralCheck> structuralChecks;
    private final Class<?> targetClass;
    private final Path sourcePath;

    private Tester(Builder builder) {
        OutputCapture outputCapture = new OutputCapture();
        this.reporter = new ResultReporter();
        this.executor = new TestExecutor(builder.testCases, outputCapture, reporter);
        this.structuralChecks = builder.structuralChecks;
        this.targetClass = builder.targetClass;
        this.sourcePath = builder.sourcePath;
    }

    /**
     * Valuta (se presenti) i vincoli strutturali e, solo se tutti superati, esegue i
     * test funzionali contro l'azione fornita.
     *
     * @return true se sia i vincoli strutturali sia tutti i test funzionali sono superati
     */
    public boolean verify(Runnable action) {
        if (!structuralChecks.isEmpty() && !runStructuralChecks()) {
            return false;
        }
        return executor.run(action);
    }

    private boolean runStructuralChecks() {
        if (sourcePath == null) {
            throw new IllegalStateException(
                    "Nessun sourcePath disponibile: imposta targetClass(...) o sourcePath(...) "
                            + "per poter usare i vincoli strutturali");
        }

        reporter.printStructuralCheckHeader();
        StructuralAnalyzer analyzer = new StructuralAnalyzer(sourcePath);

        List<StructuralCheckResult> failedResults = new ArrayList<>();
        for (StructuralCheck check : structuralChecks) {
            StructuralCheckResult result = check.evaluate(analyzer);
            reporter.printStructuralCheckResult(check.description(), result);
            if (!result.passed()) {
                failedResults.add(result);
            }
        }

        if (!failedResults.isEmpty()) {
            reporter.printStructuralChecksFailedSummary(failedResults);
            return false;
        }
        return true;
    }

    /** La classe dello studente associata a questa sessione di test, se impostata. */
    public Class<?> getTargetClass() {
        return targetClass;
    }

    /** Il percorso del file .java della classe dello studente, se impostato/dedotto. */
    public Path getSourcePath() {
        return sourcePath;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Deduce il percorso del file .java a partire dal nome pienamente qualificato
     * della classe, assumendo una struttura standard Maven/Gradle (src/main/java/...).
     * Utile per non dover specificare a mano il sourcePath quando si vuole abilitare
     * l'analisi strutturale.
     */
    public static Path sourcePathOf(Class<?> clazz) {
        String relativePath = clazz.getName().replace('.', '/') + ".java";
        return Path.of("src/main/java", relativePath);
    }

    public static class Builder {
        private final List<TestCase> testCases = new ArrayList<>();
        private final List<StructuralCheck> structuralChecks = new ArrayList<>();
        private Class<?> targetClass;
        private Path sourcePath;

        public Builder testCases(List<TestCase> testCases) {
            this.testCases.clear();
            this.testCases.addAll(testCases);
            return this;
        }

        public Builder targetClass(Class<?> targetClass) {
            this.targetClass = targetClass;
            if (this.sourcePath == null) {
                this.sourcePath = sourcePathOf(targetClass);
            }
            return this;
        }

        /** Da usare solo se il file .java non si trova nel percorso standard dedotto da targetClass. */
        public Builder sourcePath(Path sourcePath) {
            this.sourcePath = sourcePath;
            return this;
        }

        /** Aggiunge un singolo vincolo strutturale da verificare prima dei test funzionali. */
        public Builder structuralCheck(StructuralCheck check) {
            this.structuralChecks.add(check);
            return this;
        }

        /** Aggiunge più vincoli strutturali in una volta. */
        public Builder structuralChecks(List<StructuralCheck> checks) {
            this.structuralChecks.addAll(checks);
            return this;
        }

        public Tester build() {
            if (testCases.isEmpty()) {
                throw new IllegalStateException("Nessun TestCase impostato: chiama testCases(...) prima di build()");
            }
            return new Tester(this);
        }
    }
}