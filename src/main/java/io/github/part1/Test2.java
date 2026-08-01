package io.github.part1;

public class Test2 {
    public static void test2() {
        int num1 = 100, num2 = 1;
        double num3 = 7.2, num4 = 4/5, num5 = 1.1;

        // Cosa stampa questo test?
        System.out.println("test2:");
        int x = 0;
        if(x + num5 > num2) {
            System.out.println(num3 + num4);
        } else {
            x = 20;
            System.out.println(x + num3);
        }
        System.out.println(num1);
        System.out.println("______________________");
    }
}
