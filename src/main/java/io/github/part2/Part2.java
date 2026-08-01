package io.github.part2;

import io.github.helper.Read;
import io.github.helper.Tester;

public class Part2 {

    /*
        ESERCIZIO: La Biglietteria del Cinema (Versione 2.0)

        Scrivi il codice dentro il metodo part2() che calcola e stampa il prezzo del biglietto
        del cinema usando le variabili già inserite.

        Il programma riceve in input 3 valori (uno per riga):
        1. L'età dello spettatore (intero)
        2. Il giorno della settimana (intero da 1 a 7, dove 7 è la domenica)
        3. Il numero della proiezione della giornata (intero da 1 in poi, es. 1 per la prima proiezione, 2 per la seconda, ecc.)

        REGOLE PER IL CALCOLO DEL PREZZO (Prezzo base = 10 euro):
        - Se lo spettatore è minorenne, il prezzo finale è dimezzato.
        - Se è domenica, il prezzo sale di 3 euro.
        - Se è la prima proiezione ED è il weekend,
          il prezzo è aumentato di altri 2 euro.
        - Se è una quarta proiezione o successiva, il prezzo scende di 2 euro
          (questo sconto si applica a prescindere dal giorno).

        Nota: Stampa a schermo il prezzo finale calcolato.
        Usa Part2 per verificare la correttezza della tua soluzione tramite i test automatici!
     */

    static void part2() {
        // Usa queste variabili già fornite, non modificare questa parte!
        //---------------------------------//
        int etaSpettatore = Read.readInt();
        int giorno = Read.readInt();
        int numeroProiezione = Read.readInt();
        //---------------------------------//

        // Scrivi qui il tuo codice
    }


    // NON TOCCARE IL MAIN, serve per i test!

    public static void main(String[] args) {
        Tester.setup();
        Tester.verify(Part2::part2);
    }
}