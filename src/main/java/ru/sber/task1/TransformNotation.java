package ru.sber.task1;

import java.util.Stack;

//1
//Написать программу, которая бы переводила целочисленное число из десятичной
// системы счисления в любую другую (от двоичной до 36-ой).
// Для системы счисления больше 10 использовать цифры + буквы английского алфавита.




public class TransformNotation {

    private int notationBasis;

    private int number;

    public TransformNotation(int notationBasis, int number) {
        this.notationBasis = notationBasis;
        this.number = number;
    }

    private char transformNumberToLetter(int number) {
        return (char) (number + 55);
    }


    public String calc() {
        int tmp = number;
        Stack<String> stack = new Stack<>();
        while (tmp != 0) {
            int tmpResult = tmp % notationBasis;
            tmp = tmp / notationBasis;
            if (tmpResult > 10) {
                stack.push(transformNumberToLetter(tmpResult) + "");
            } else {
                stack.push(tmpResult + "");
            }
        }
        StringBuilder result = new StringBuilder();
        while (!stack.empty()) {
            result.append(stack.pop());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new TransformNotation(16, 23654).calc());
    }

}
