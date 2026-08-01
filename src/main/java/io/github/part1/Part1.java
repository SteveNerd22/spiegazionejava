package io.github.part1;
import io.github.helper.Read;

public class Part1 {
    static final int NUM_TEST = 3;

    public static void main(String[] args) {
        boolean rerun;
        boolean selected;
        String userInput;

        do {
            selected = false;
            int test = 1;

            do {
                userInput = Read.readString("Seleziona il test:").trim();
                System.out.println();
                for(int i = 1; i <= NUM_TEST; i++) {
                    selected = userInput.matches("" + i);
                    if(selected)
                        break;
                }

                if (!selected)
                    System.out.println("`" + userInput + "` non e' un valore riconosciuto, inserisci un numero da 1 a "+ NUM_TEST +"\n");
                else
                    test = Integer.parseInt(userInput);
            } while (!selected);

            System.out.println("\n");

            switch (test) {
                case 1:
                    Test1.test1();
                    break;
                case 2:
                    Test2.test2();
                    break;
                case 3:
                    Test3.test3();
                    break;
                default:
                    System.out.println("Valore non previsto");
            }

            do {
                userInput = Read.readString("\nVuoi eseguire un altro test? (y/n):").trim().toLowerCase();
                selected = userInput.matches("y") || userInput.matches("n");

                if (!selected) System.out.println("`" + userInput + "` non e' un valore riconosciuto, inserisci y (yes) o n (no)\n");
            } while (!selected);

            rerun = userInput.matches("y");
        } while(rerun);
    }
}
