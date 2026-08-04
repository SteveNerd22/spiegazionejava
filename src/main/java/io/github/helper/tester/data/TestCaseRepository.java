package io.github.helper.tester.data;

import io.github.helper.tester.TestCase;

import java.util.List;

/**
 * Contiene i dataset di test per i singoli esercizi. Tenerli qui, separati da Tester,
 * evita che la classe che esegue i test debba crescere ad ogni nuovo esercizio aggiunto
 * (prima ogni "parte" diventava un nuovo metodo setupPartN dentro Tester).
 */
public final class TestCaseRepository {

    private TestCaseRepository() {
        // utility class
    }

    public static List<TestCase> part2() {
        return List.of(
                // --- TEST PUBBLICI (5) ---
                TestCase.pub("10.0", 30, 2, 1),
                TestCase.pub("15.0", 30, 7, 1),
                TestCase.pub("4.0", 15, 3, 4),
                TestCase.pub("5.0", 10, 1, 1),
                TestCase.pub("10.0", 25, 6, 2),

                // --- TEST SEGRETI (10) ---
                TestCase.secret("7.5", 12, 7, 1),
                TestCase.secret("12.0", 40, 6, 1),
                TestCase.secret("8.0", 40, 6, 4),
                TestCase.secret("5.5", 14, 7, 4),
                TestCase.secret("13.0", 35, 7, 2),
                TestCase.secret("4.0", 16, 3, 5),
                TestCase.secret("8.0", 50, 4, 10),
                TestCase.secret("6.0", 15, 6, 1),
                TestCase.secret("11.0", 30, 7, 5),
                TestCase.secret("5.5", 13, 7, 6)
        );
    }

    public static List<TestCase> part3() {
        return List.of(
                // --- TEST PUBBLICI (5) ---
                // Cuscino: 5, Stress: 1 (Tranquillo), Cappelli: 2 -> 10 + (5*1.5) + (2*3) = 23.5
                TestCase.pub("23.5", 5, 1, 2),

                // Cuscino: 10, Stress: 2 (Stressato), Cappelli: 0 -> 10 + (10*1.5) + 12 = 37.0
                TestCase.pub("37.0", 10, 2, 0),

                // Cuscino: 2, Stress: 3 (Crisi), Cappelli: 5 -> 10 + (2*1.5) + 25 + (5*3) = 53.0
                TestCase.pub("53.0", 2, 3, 5),

                // Cuscino: 0, Stress: 1, Cappelli: 0 -> 10.0
                TestCase.pub("10.0", 0, 1, 0),

                // Cuscino: 50, Stress: 3, Cappelli: 7 -> supera 100 -> 100.0
                TestCase.pub("100.0", 50, 3, 7),

                // --- TEST SEGRETI (10) ---
                TestCase.secret("37.0", 4, 2, 3),
                TestCase.secret("35.5", 15, 1, 1),
                TestCase.secret("59.0", 8, 3, 4),
                TestCase.secret("40.0", -7, 2, 6),
                TestCase.secret("65.0", 20, 3, 0),
                TestCase.secret("11.5", 1, 1, 0),
                TestCase.secret("82.0", 30, 2, 5),
                TestCase.secret("50.0", 6, 3, 2),
                TestCase.secret("52.0", 12, 2, 4),
                TestCase.secret("100.0", 60, 3, 7)
        );
    }
}