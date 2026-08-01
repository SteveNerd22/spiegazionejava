package io.github.part1;

public class Test3 {
    public static void test3() {
        int num1 = 10, num2 = 5, num3 = 100, num4 = -6, num5 = 1;
        double num6 = 7.2, num7 = 3.81, num8 = 3.3333, num9 = 4/5, num10 = 1.1;

        // Cosa stampa questo test?
        System.out.println("test3");
        if(num3 > 90 && num3 < 70)
            System.out.println(num8 + num7);
        if(num7 < num4 || false)
            System.out.printf("num2");
        if(num3 - num5 != 99 || num3 == num1 * num1)
            System.out.println("num7");
        System.out.println("______________________");
    }
}
