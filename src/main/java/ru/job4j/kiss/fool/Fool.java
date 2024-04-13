package ru.job4j.kiss.fool;

import java.util.Scanner;

public class Fool {
    public static String answerPC(Integer startAt) {
        String rsl = String.valueOf(startAt);
        if (startAt % 3 == 0) {
            rsl = "Fizz";
        }
        if (startAt % 5 == 0) {
            rsl = "Buzz";
        }
        if (startAt % 3 == 0 && startAt % 5 == 0) {
            rsl = "FizzBuzz";
        }
        return rsl;
    }

    public static int answerUser(String answer, Integer startAt) {
        int rsl = startAt + 1;
        String correctAnswer = answerPC(startAt);
        if (!correctAnswer.equals(answer)) {
            rsl = 1;
            System.out.println("Ошибка. Начинай снова.");
        }
        return rsl;
    }

    public static void main(String[] args) {
        System.out.println("Игра FizzBuzz.");
        var startAt = 1;
        var input = new Scanner(System.in);
        while (startAt < 100) {
            System.out.println(answerPC(startAt));
            startAt++;
            var answer = input.nextLine();
            startAt = answerUser(answer, startAt);
        }
    }
}
