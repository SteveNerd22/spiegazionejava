package io.github.helper;

import java.util.Scanner;


import java.util.Scanner;

import static io.github.helper.IoContext.getScanner;

public class Read {
        public static int readInt() {
            return getScanner().nextInt();
        }

        public static char readChar() {
            return getScanner().next().toCharArray()[0];
        }

        public static double readDouble() {
            return getScanner().nextDouble();
        }

        public static float readFloat() {
            return getScanner().nextFloat();
        }

        public static boolean readBoolean() {
            return getScanner().nextBoolean();
        }

        public static String readString() {
            return getScanner().next();
        }

        public static int readInt(String text) {
            System.out.print(text);
            return getScanner().nextInt();
        }

        public static char readChar(String text) {
            System.out.print(text);
            return getScanner().next().toCharArray()[0];
        }

        public static double readDouble(String text) {
            System.out.print(text);
            return getScanner().nextDouble();
        }

        public static float readFloat(String text) {
            System.out.print(text);
            return getScanner().nextFloat();
        }

        public static boolean readBoolean(String text) {
            System.out.print(text);
            return getScanner().nextBoolean();
        }

        public static String readString(String text) {
            System.out.print(text);
            return getScanner().next();
        }
    }