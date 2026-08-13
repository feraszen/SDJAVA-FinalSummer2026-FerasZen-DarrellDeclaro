package com.keyingym.console;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

/** Centralizes safe console input parsing for the application menus. */
public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /** Reads a line without modifying the value. Useful for passwords. */
    public String readRawLine() {
        if (!scanner.hasNextLine()) return null;
        return scanner.nextLine();
    }

    /** Reads and trims a normal console value. */
    public String readLine() {
        String value = readRawLine();
        return value == null ? null : value.trim();
    }

    public Integer readInteger() {
        String value = readLine();
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public BigDecimal readBigDecimal() {
        String value = readLine();
        if (value == null || value.isBlank()) return null;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public LocalDateTime readDateTime() {
        String value = readLine();
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
            return null;
        }
    }
}
