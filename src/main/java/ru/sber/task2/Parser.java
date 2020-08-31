package ru.sber.task2;
//2
//Есть файл, в котором построчно располагаются простые примеры типа Число1ЗнакЧисло2. Числа типа long, знаки: +,-,*,/
//Например:
//30*20
//188-53
//160/2
//Нужно вычислить эти значения и записать в другой файл:
//600
//135
//80
//Предусмотреть, что в выражении могут быть умышленные ошибки и его вычислить нельзя (10/0, 10/а, 10//10 и пр). В этом случае вывести, что за ошибка в примере.
public class Parser {

    private static class Result {
        private double acc;
        private String rest;
        private Result(double acc, String rest) {
            this.acc = acc;
            this.rest = rest;
        }
    }

    public double parse(String s) throws Exception {
        Result result = calc(s);
        if (!result.rest.isEmpty()) {
            throw new Exception("Can't parse, rest: " + result.rest);
        }
        return result.acc;
    }

    private Result calc(String s) throws Exception {
        Result current = num(s);
        double acc;
        if (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (!(sign == '+' ||
                    sign == '-' ||
                    sign == '*' ||
                    sign == '/')) {
                throw new Exception("Don't right sign after number");
            }
            String next = current.rest.substring(1);
            acc = current.acc;
            current = num(next);
            if (sign == '+') {
                acc += current.acc;
            } else if (sign == '-') {
                acc -= current.acc;
            } else if (sign == '*') {
                acc *= current.acc;
            } else if (sign == '/') {
                if (current.acc == 0) {
                    throw new Exception("Divide by zero");
                }
                acc /= current.acc;
            } else {
                throw new Exception("Don't allowed operation");
            }
            current.acc = acc;
        }
        return new Result(current.acc, current.rest);
    }

    private Result num(String s) throws Exception {
        int i = 0;
        boolean negative = false;
        // число также может начинаться с минуса
        if (s.charAt(0) == '-') {
            negative = true;
            s = s.substring(1);
        }
        // разрешаем только цифры
        while (i < s.length() && (Character.isDigit(s.charAt(i)))) {
            i++;
        }
        if (i == 0) { // что-либо похожее на число мы не нашли
            throw new Exception("can't get valid number in '" + s + "'");
        }
        long dPart;
        try {
            dPart = Long.parseLong(s.substring(0, i));
        } catch (NumberFormatException e) {
            throw new Exception("Invalid long value");
        }
        if (negative) {
            dPart = -dPart;
        }
        String restPart = s.substring(i);
        return new Result(dPart, restPart);
    }


    public static void main(String[] args) throws Exception {
        System.out.println(new Parser().parse("100//9"));
    }
}