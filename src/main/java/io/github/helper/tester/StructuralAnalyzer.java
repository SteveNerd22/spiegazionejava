package io.github.helper.tester;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Motore generico per l'analisi strutturale del sorgente di uno studente.
 * <p>
 * Non conosce il significato di alcun vincolo specifico (non sa cosa sia una "print" o
 * un "ciclo"): espone solo primitive di navigazione dell'AST (JavaParser). L'implementazione
 * concreta dei singoli vincoli vive nelle factory statiche di {@link TestCase}, che usano
 * queste primitive per costruire {@link StructuralCheck} di dominio.
 */
public class StructuralAnalyzer {

    private final CompilationUnit compilationUnit;

    public StructuralAnalyzer(Path sourcePath) {
        try {
            this.compilationUnit = StaticJavaParser.parse(new File(sourcePath.toString()));
        } catch (Exception e) {
            throw new IllegalStateException("Impossibile leggere o analizzare il sorgente: " + sourcePath, e);
        }
    }

    /** Ritorna la dichiarazione del metodo richiesto (il primo che matcha per nome). */
    public MethodDeclaration getMethod(String methodName) {
        List<MethodDeclaration> matches = new ArrayList<>();
        compilationUnit.findAll(MethodDeclaration.class).forEach(m -> {
            if (m.getNameAsString().equals(methodName)) {
                matches.add(m);
            }
        });
        if (matches.isEmpty()) {
            throw new IllegalStateException("Metodo '" + methodName + "' non trovato nel sorgente");
        }
        return matches.get(0);
    }

    /**
     * Ritorna, nel corpo del metodo indicato, tutte le chiamate a metodo che soddisfano
     * il predicato dato. A differenza di {@link #countMethodCalls}, espone i nodi stessi
     * (utile per costruire messaggi di errore dettagliati con riga e snippet di codice).
     */
    public List<MethodCallExpr> findMethodCalls(String methodName, Predicate<MethodCallExpr> predicate) {
        MethodDeclaration method = getMethod(methodName);
        List<MethodCallExpr> found = new ArrayList<>();

        method.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(MethodCallExpr call, Void arg) {
                super.visit(call, arg);
                if (predicate.test(call)) {
                    found.add(call);
                }
            }
        }, null);

        return found;
    }

    /**
     * Conta, nel corpo del metodo indicato, quante chiamate a metodo soddisfano il predicato dato.
     * Il predicato decide COSA cercare (es. "è una chiamata a System.out.println"): questa
     * primitiva sa solo attraversare l'AST e contare.
     */
    public int countMethodCalls(String methodName, Predicate<MethodCallExpr> predicate) {
        return findMethodCalls(methodName, predicate).size();
    }

    /**
     * Ritorna tutti i nodi di un certo tipo trovati nel corpo del metodo indicato.
     * Utile per vincoli generici come "nessun ciclo" (cercando ForStmt/WhileStmt/DoStmt)
     * o altre analisi strutturali future, senza dover aggiungere un metodo dedicato qui
     * ogni volta.
     */
    public <T extends Node> List<T> findInMethod(String methodName, Class<T> nodeType) {
        MethodDeclaration method = getMethod(methodName);
        return method.findAll(nodeType);
    }
}