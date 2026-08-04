package io.github.helper.tester;

/**
 * Esito della valutazione di uno {@link StructuralCheck}.
 *
 * @param passed  true se il vincolo è stato rispettato
 * @param message dettaglio leggibile dell'esito (es. "Trovate 3 chiamate a println, massimo 1")
 */
public record StructuralCheckResult(boolean passed, String message) {
}