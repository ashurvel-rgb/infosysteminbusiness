package ru.edu.pr01;

public class SelfCheck {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        check("типовой расчёт", closeTo(OrderCalculator.calculateTotal(2, 100.0, 10.0, 20.0), 216.0));
        check("нижняя граница количества", OrderCalculator.isValid(1, 100.0, 0.0));
        check("верхняя граница количества", OrderCalculator.isValid(10_000, 100.0, 0.0));
        check("нулевое количество отклоняется", !OrderCalculator.isValid(0, 100.0, 0.0));
        check("количество выше максимума отклоняется", !OrderCalculator.isValid(10_001, 100.0, 0.0));
        check("верхняя граница цены", OrderCalculator.isValid(1, 5_000_000.0, 0.0));
        check("нулевая цена отклоняется", !OrderCalculator.isValid(1, 0.0, 0.0));
        check("нижняя граница скидки", OrderCalculator.isValid(1, 100.0, 0.0));
        check("вариант 2: скидка 15 процентов допустима", OrderCalculator.isValid(1, 100.0, 15.0));
        check("вариант 2: скидка выше 15 процентов отклоняется", !OrderCalculator.isValid(1, 100.0, 15.01));
        check("НДС начисляется после скидки", closeTo(OrderCalculator.calculateVat(90.0, 20.0), 18.0));
        check("NaN в цене отклоняется", !OrderCalculator.isValid(1, Double.NaN, 0.0));
        check("цена выше максимума", !OrderCalculator.isValid(1, 5_000_000.01, 0.0));
        check("цена 0.01 допустима", OrderCalculator.isValid(1, 0.01, 0.0));
        check("отрицательная цена", !OrderCalculator.isValid(1, -1.0, 0.0));
        check("отрицательная скидка", !OrderCalculator.isValid(1, 100.0, -0.01));
        check("бесконечная цена", !OrderCalculator.isValid(1, Double.POSITIVE_INFINITY, 0.0));
        check("NaN в скидке", !OrderCalculator.isValid(1, 100.0, Double.NaN));
        check("нулевой НДС", OrderCalculator.isValidVat(0.0));
        check("верхняя граница НДС", OrderCalculator.isValidVat(100.0));
        check("НДС выше границы", !OrderCalculator.isValidVat(100.01));
        check("отрицательный НДС", !OrderCalculator.isValidVat(-0.01));
        check("бесконечный НДС", !OrderCalculator.isValidVat(Double.POSITIVE_INFINITY));
        check("NaN в НДС", !OrderCalculator.isValidVat(Double.NaN));
        check("максимальная базовая сумма", closeTo(OrderCalculator.calculateBase(10_000, 5_000_000.0), 50_000_000_000.0));
        check("скидка 15 процентов", closeTo(OrderCalculator.calculateTotal(1, 100.0, 15.0, 20.0), 102.0));
        check("повтор одинакового расчёта", closeTo(OrderCalculator.calculateTotal(2, 100.0, 10.0, 20.0), 216.0));
        checkThrows("ошибочный расчёт не выполняется", new Runnable() {
            @Override
            public void run() {
                OrderCalculator.calculateTotal(0, 100.0, 0.0, 20.0);
            }
        });

        System.out.println("Итог: " + passed + " успешно, " + failed + " ошибок.");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static boolean closeTo(double actual, double expected) {
        return Math.abs(actual - expected) < 0.001;
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void checkThrows(String name, Runnable action) {
        try {
            action.run();
            check(name, false);
        } catch (IllegalArgumentException expected) {
            check(name, true);
        }
    }
}
