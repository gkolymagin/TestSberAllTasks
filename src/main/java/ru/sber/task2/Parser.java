package ru.sber.task2;

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
        while (current.rest.length() > 0) {
            if (!(current.rest.charAt(0) == '+' ||
                    current.rest.charAt(0) == '-' ||
                    current.rest.charAt(0) == '*' ||
                    current.rest.charAt(0) == '/')) {
                break;
            }
            char sign = current.rest.charAt(0);
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