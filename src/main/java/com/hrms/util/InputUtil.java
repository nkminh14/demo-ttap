package com.hrms.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public static String getNonEmptyString(String prompt) {
        String input;
        do {
            input = getString(prompt);
            if (input.isEmpty()) {
                System.out.println("Vui lòng nhập giá trị hợp lệ!");
            }
        } while (input.isEmpty());
        return input;
    }
    
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ!");
            }
        }
    }
    
    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số thực hợp lệ!");
            }
        }
    }
    
    public static LocalDate getDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (yyyy-MM-dd): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Vui lòng nhập ngày theo định dạng yyyy-MM-dd!");
            }
        }
    }
    
    public static LocalTime getTime(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (HH:mm): ");
                String input = scanner.nextLine().trim();
                return LocalTime.parse(input, TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Vui lòng nhập thời gian theo định dạng HH:mm!");
            }
        }
    }
    
    public static boolean getBoolean(String prompt) {
        while (true) {
            String input = getString(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("true")) {
                return true;
            } else if (input.equals("n") || input.equals("no") || input.equals("false")) {
                return false;
            } else {
                System.out.println("Vui lòng nhập y hoặc n!");
            }
        }
    }
    
    public static void pressEnterToContinue() {
        System.out.println("Nhấn Enter để tiếp tục...");
        scanner.nextLine();
    }
    
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
