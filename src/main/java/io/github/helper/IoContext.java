package io.github.helper;

import java.io.InputStream;
import java.util.Scanner;

public class IoContext {
    private static Scanner scanner = new Scanner(System.in);

    public static void setInputStream(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    public static Scanner getScanner() {
        return scanner;
    }
}