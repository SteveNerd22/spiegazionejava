package io.github.part3;

import io.github.helper.Read;
import io.github.helper.Tester;

public class Part3 {

    /*
        ESERCIZIO: La Diagnosi della Calvizie di Leo - Percentuale di Perdita

        Scrivi il codice dentro il metodo part3() che calcola e stampa la percentuale
        stimata di capelli persi usando le variabili già inserite.

        Il programma riceve in input 3 valori (uno per riga):
        1. Quantità di capelli trovata sul cuscino la mattina (intero)
        2. Livello di stress quotidiano (intero da 1 a 3, dove 1 = Tranquillo, 2 = Stressato, 3 = Crisi esistenziale)
        3. Numero di cappelli/berretti indossati a settimana (intero da 0 a 7)

        REGOLE PER IL CALCOLO DELLA PERCENTUALE (Base = 10.0%):
        - Ogni capello sul cuscino aumenta la percentuale del 1.5%.
        - Se lo stress è 2, si aggiunge un +12.0% alla percentuale.
        - Se lo stress è 3 (crisi esistenziale), si aggiunge un +25.0%.
        - Per ogni cappello indossato a settimana, la percentuale aumenta di un ulteriore +3.0%.
        - SOGLIE LIMITE: La percentuale finale non può mai superare il 100.0%
          (se supera 100, si ferma a 100.0) e non può scendere sotto lo 0.0%.

        Nota: Stampa a schermo la percentuale finale calcolata come double.
        Usa Part3 per verificare la correttezza della tua soluzione tramite i test automatici!
     */

    static void part3() {
        // Usa queste variabili già fornite, non modificare questa parte!
        //---------------------------------//
        int capelliCuscino = Read.readInt();
        int livelloStress = Read.readInt();
        int numeroCappelli = Read.readInt();
        //---------------------------------//

        // Scrivi qui il tuo codice
    }


    // NON TOCCARE IL MAIN, serve per i test!

    public static void main(String[] args) {
        Tester.setupPart3();
        Tester.verify(Part3::part3);
    }
}