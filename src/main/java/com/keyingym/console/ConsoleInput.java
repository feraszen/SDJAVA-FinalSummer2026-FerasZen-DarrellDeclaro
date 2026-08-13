package com.keyingym.console;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Centralizes safe console input parsing for the application menus.
 */
public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    public String readLine() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public Integer readInteger() {
        String input = readLine();
        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public BigDecimal readBigDecimal() {
        String input = readLine();
        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return new BigDecimal(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public LocalDateTime readDateTime() {
        String input = readLine();
        if (input == null || input.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(input);
        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
            return null;
        }
    }
}
