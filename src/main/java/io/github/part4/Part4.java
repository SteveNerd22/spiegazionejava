package io.github.part4;

import io.github.helper.Read;
import io.github.helper.tester.TestCase;
import io.github.helper.tester.Tester;
import io.github.helper.ExerciseRegistry;
import io.github.helper.tester.data.TestCaseRepository;

public class Part4 {

    /*
        ESERCIZIO: Laboratorio di Pattern e Iterazioni Avanzate

        Benvenuto nella quarta parte del laboratorio! Questa volta metterai alla prova
        le tue capacità di gestione dei cicli annidati e della formattazione dell'output
        tramite pattern geometrici e matrici numeriche.

        Scegli quale esercizio svolgere modificando la costante METODO_DA_TESTARE
        e implementa la logica all'interno del metodo corrispondente.
        Usa Part4 per verificare la correttezza della tua soluzione tramite i test automatici!
     */

    // =========================================================================
    // CONFIGURAZIONE: Scrivi qui quale esercizio vuoi testare ("test1", "test2", "test3")
    // =========================================================================
    private static final String METODO_DA_TESTARE = "test1";

    /*
        =========================================================================
        TEST 1: Il Rombo di Asterischi e Cancelletti
        =========================================================================
        Scrivi il codice dentro il metodo test1() che riceve in input un numero
        intero dispari N (es. 5) e stampa a schermo il seguente pattern a rombo
        usando ESATTAMENTE UN SOLO print (o println) all'interno di tutto il metodo.

        Il programma riceve in input 1 valore:
        1. Numero intero positivo dispari N (es. 5)

        |Esempio con N = 5:
        |*##
        |**##
        |***##
        |**##
        |*##

        Attenzione:
        - Devi controllare che N abbia un valore valido, se non ha un valore valido il codice non deve stampare nulla
        Regole:
        - Vietato l'uso di più istruzioni di stampa (usa massimo 1 print/println).
        - Gestisci correttamente gli spazi e i caratteri speciali ('*' e '#').
        Note:
        - Le linee verticali nell'esempio non vanno stampate, servono solo per aiutare a contare gli spazi
     */
    static void test1() {
        // Usa queste variabili già fornite, non modificare questa parte!
        //---------------------------------//
        int n = Read.readInt();
        //---------------------------------//

        // Scrivi qui il tuo codice per il test 1
    }

    /*
        =========================================================================
        TEST 2: La Cornice Doppia
        =========================================================================
        Scrivi il codice dentro il metodo test2() che riceve in input due numeri
        interi: larghezza e altezza (es. 6 e 4). Il programma deve stampare una
        cornice rettangolare con bordo esterno fatto di '@' e interno di '-'
        usando un massimo di 2 print nel metodo.

        Il programma riceve in input 2 valori (uno per riga):
        1. Larghezza del rettangolo (intero non negativo)
        2. Altezza del rettangolo (intero non negativo)

        |Esempio con larghezza = 6, altezza = 4:
        |@@@@@@
        |@----@
        |@----@
        |@@@@@@

        Regole:
        - Massimo 2 chiamate a print/println in tutto il metodo.
        Note:
        - Le linee verticali nell'esempio non vanno stampate, servono solo per aiutare a contare gli spazi
     */
    static void test2() {
        // Usa queste variabili già fornite, non modificare questa parte!
        //---------------------------------//
        int larghezza = Read.readInt();
        int altezza = Read.readInt();
        //---------------------------------//

        // Scrivi qui il tuo codice per il test 2
    }

    /*
        =========================================================================
        TEST 3: La Matrice Scivolante
        =========================================================================
        Scrivi il codice dentro il metodo test3() che riceve in input un numero
        intero N (es. 4) e stampa una sequenza di righe numeriche in cui i
        numeri "scivolano" progressivamente verso sinistra, usando al massimo 1 print.

        Il programma riceve in input 1 valore:
        1. Numero intero positivo N da 2 a infinito (es. 4)

        |Esempio con N = 4:
        |1234
        |2341
        |3412
        |4123
        |1234

        Attenzione:
        - Devi controllare che N abbia un valore valido, se non ha un valore valido il codice non deve stampare nulla
        Regole:
        - Massimo 1 chiamata a print/println.
        Note:
        - Le linee verticali nell'esempio non vanno stampate, servono solo per aiutare a contare gli spazi
     */
    static void test3() {
        // Usa queste variabili già fornite, non modificare questa parte!
        //---------------------------------//
        int n = Read.readInt();
        //---------------------------------//

        // Scrivi qui il tuo codice per il test 3
    }

    // =========================================================================
    // MAIN PER I TEST AUTOMATICI (NON MODIFICARE)
    // =========================================================================
    public static void main(String[] args) {
        ExerciseRegistry registry = new ExerciseRegistry()
                .register("test1", Part4::test1, "test1", 1)
                .register("test2", Part4::test2, "test2", 2)
                .register("test3", Part4::test3, "test3", 1);

        ExerciseRegistry.ExerciseConfig config = registry.select(METODO_DA_TESTARE);

        Tester tester = Tester.builder()
                .targetClass(Part4.class)
                .structuralCheck(TestCase.maxPrintCalls(config.getMethodName(), config.getMaxPrints()))
                .testCases(TestCaseRepository.part4(config.getMethodName()))
                .build();

        tester.verify(config.getRunnable());
    }
}