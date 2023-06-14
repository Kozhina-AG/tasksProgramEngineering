package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Test {
    private ArrayList<Question> test = new ArrayList<>();

    public Test() {
        load("C:\\Users\\admin\\Desktop\\task11\\src\\test.txt");
    }

    /**
     * Создает тест с указанным количеством вопросов
     *
     * @param numberQuest Количество вопросов в тесте
     * @return Список вопросов для теста
     */
    public ArrayList<Question> createTest(int numberQuest) {
        ArrayList<Question> currentTest = new ArrayList<>(test); // Создаем копию исходного списка вопросов

        Collections.shuffle(currentTest);

        return new ArrayList<>(currentTest.subList(0, Math.min(numberQuest, currentTest.size())));
    }

    /**
     * Возвращает список всех вопросов
     *
     * @return Список всех вопросов
     */
    public ArrayList<Question> getTest() { //Метод, который возвращает список всех вопросов.
        return test;
    }

    /**
     * Загружает вопросы из файла
     *
     * @param filename Путь к файлу с вопросами
     */
    private void load(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                Question question = new Question("");
                question.setQuestion(line);

                while (scanner.hasNextLine()) {
                    line = scanner.nextLine().trim();
                    if (line.equalsIgnoreCase("#badQuestions")) {
                        break; // Завершаем цикл, если достигли метки #badQuestions
                    }
                    question.addTrue(line); // Добавляем правильный ответ
                }

                while (scanner.hasNextLine()) {
                    line = scanner.nextLine().trim();
                    if (line.equalsIgnoreCase("#nextQuestion")) {
                        break; // Завершаем цикл, если достигли метки #nextQuestion
                    }
                    question.addFalse(line); // Добавляем неправильный ответ
                }

                test.add(question); // Добавляем вопрос в список теста
            }

            System.out.println("Файл прочитан успешно!");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не обнаружен");
        }
    }
}
