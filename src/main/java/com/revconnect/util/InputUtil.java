package com.revconnect.util;

import java.util.Scanner;

public class InputUtil {

    private InputUtil() {

    }

    public static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Enter a valid number: ");
            }
        }
    }

    public static String readNonEmpty(Scanner sc) {
        while (true) {
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.print("❌ Input cannot be empty: ");
        }
    }
}
