package ru.edu.pr01;

import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10_000;
    public static final double MAX_UNIT_PRICE = 5_000_000.0;

    // Индивидуальный вариант 2: максимальная скидка равна 15 %.
    public static final double MAX_DISCOUNT_PERCENT = 15.0;
    public static final double MIN_VAT_PERCENT = 0.0;
    public static final double MAX_VAT_PERCENT = 100.0;

    public static boolean isValid(int quantity, double unitPrice, double discountPercent) {
        return quantity >= MIN_QUANTITY
                && quantity <= MAX_QUANTITY
                && Double.isFinite(unitPrice)
                && unitPrice > 0.0
                && unitPrice <= MAX_UNIT_PRICE
                && Double.isFinite(discountPercent)
                && discountPercent >= 0.0
                && discountPercent <= MAX_DISCOUNT_PERCENT;
    }

    public static double calculateBase(int quantity, double unitPrice) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Количество должно быть от 1 до 10000.");
        }
        if (!Double.isFinite(unitPrice) || unitPrice <= 0.0 || unitPrice > MAX_UNIT_PRICE) {
            throw new IllegalArgumentException("Цена должна быть больше 0 и не превышать 5000000 рублей.");
        }
        return quantity * unitPrice;
    }

    public static double applyDiscount(double base, double discountPercent) {
        if (!Double.isFinite(base) || base < 0.0) {
            throw new IllegalArgumentException("Базовая сумма не может быть отрицательной.");
        }
        if (!Double.isFinite(discountPercent)
                || discountPercent < 0.0
                || discountPercent > MAX_DISCOUNT_PERCENT) {
            throw new IllegalArgumentException("Скидка должна быть от 0 до " + MAX_DISCOUNT_PERCENT + " процентов.");
        }
        return base * (1.0 - discountPercent / 100.0);
    }

    public static double calculateVat(double discounted, double vatPercent) {
        if (!Double.isFinite(discounted) || discounted < 0.0) {
            throw new IllegalArgumentException("Сумма после скидки не может быть отрицательной.");
        }
        if (!isValidVat(vatPercent)) {
            throw new IllegalArgumentException("Ставка НДС должна быть от 0 до 100 процентов.");
        }
        return discounted * vatPercent / 100.0;
    }

    public static double calculateTotal(int quantity, double unitPrice, double discountPercent, double vatPercent) {
        if (!isValid(quantity, unitPrice, discountPercent)) {
            throw new IllegalArgumentException("Параметры заказа находятся вне допустимого диапазона.");
        }
        if (!isValidVat(vatPercent)) {
            throw new IllegalArgumentException("Ставка НДС должна быть от 0 до 100 процентов.");
        }

        double base = calculateBase(quantity, unitPrice);
        double discounted = applyDiscount(base, discountPercent);
        double vat = calculateVat(discounted, vatPercent);
        return discounted + vat;
    }

    public static boolean isValidVat(double vatPercent) {
        return Double.isFinite(vatPercent)
                && vatPercent >= MIN_VAT_PERCENT
                && vatPercent <= MAX_VAT_PERCENT;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int quantity = readInt(scanner, "Количество: ");
            double unitPrice = readDouble(scanner, "Цена за единицу, руб.: ");
            double discountPercent = readDouble(scanner, "Скидка, %: ");
            double vatPercent = readDouble(scanner, "Ставка НДС, %: ");

            if (!isValid(quantity, unitPrice, discountPercent)) {
                System.out.println("Ошибка: количество, цена или скидка находятся вне допустимого диапазона.");
                return;
            }
            if (!isValidVat(vatPercent)) {
                System.out.println("Ошибка: ставка НДС должна быть от 0 до 100 процентов.");
                return;
            }

            double base = calculateBase(quantity, unitPrice);
            double discounted = applyDiscount(base, discountPercent);
            double discountAmount = base - discounted;
            double vat = calculateVat(discounted, vatPercent);
            double total = discounted + vat;

            Locale outputLocale = new Locale("ru", "RU");
            System.out.printf(outputLocale, "Базовая сумма: %.2f руб.%n", base);
            System.out.printf(outputLocale, "Скидка: %.2f руб.%n", discountAmount);
            System.out.printf(outputLocale, "НДС: %.2f руб.%n", vat);
            System.out.printf(outputLocale, "Итого: %.2f руб.%n", total);
        } catch (NumberFormatException exception) {
            System.out.println("Ошибка: введите число в требуемом формате.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Ошибка: " + exception.getMessage());
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(readLine(scanner));
    }

    private static double readDouble(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = readLine(scanner).replace(',', '.');
        return Double.parseDouble(value);
    }

    private static String readLine(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Отсутствует обязательное входное значение.");
        }
        String value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Обязательное значение не должно быть пустым.");
        }
        return value;
    }
}
