package model;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Student extends Colleague {
    private int NUMBER = 5;

    public Student(Mediator mediator) {
        super(mediator);
    }

    /**
     /* Метод, который отображает вопросы и ответы на экране студента.
     */
    @Override
    public void notifyColleague(ArrayList<Question> message) {

        VBox questionPane = new VBox();
        Random random = new Random();

        for (int qi = 0; qi < NUMBER && qi < message.size(); qi++) { //Цикл, который проходит по списку вопросов и создает элементы интерфейса для каждого вопроса.
            Label questionField = new Label(); //Создание метки для отображения текста вопроса.
            questionField.textProperty().bind(message.get(qi).questionProperty());
            questionPane.getChildren().add(questionField);

            Separator separator = new Separator();
            separator.setMaxWidth(20);
            questionPane.getChildren().add(separator);

            CheckBox goodAnswersCheckBox = new CheckBox();
            CheckBox[] badAnswersCheckBox = new CheckBox[3];

            int k = random.nextInt(message.get(qi).getAnswerGood().size());

            goodAnswersCheckBox.textProperty().bind(message.get(qi).getAnswerGood().get(k));

            goodAnswersCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {BackgroundFill background_fill = new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY);

                if (new_val) {
                    goodAnswersCheckBox.setBackground(new Background(background_fill));
                    for (int i = 0; i < 3; i++) {
                        badAnswersCheckBox[i].setSelected(false);
                    }
                }
            });

            int[] count = new int[message.get(qi).getBadAnswer().size()];

            for (int i = 0; i < message.get(qi).getBadAnswer().size(); i++) {
                count[i] = 1;
            }

            for (int i = 0; i < 3; i++) {
                k = random.nextInt(message.get(qi).getBadAnswer().size());

                if (count[k] == 1) {
                    CheckBox cb = badAnswersCheckBox[i] = new CheckBox();
                    badAnswersCheckBox[i].textProperty().bind(message.get(qi).getBadAnswer().get(k));
                    badAnswersCheckBox[i].selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
                        BackgroundFill background_fill = new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY);

                        if (new_val) {
                            goodAnswersCheckBox.setSelected(false);
                            for (int j = 0; j < 3; j++) {
                                if (badAnswersCheckBox[j] != cb) {
                                    badAnswersCheckBox[j].setSelected(false);
                                }
                            }
                            cb.setBackground(new Background(background_fill));
                        }
                    });
                    count[k] = 0;
                } else {
                    i--;
                }
            }

            k = random.nextInt(4);

            for (int i = 0; i < 3; i++) {
                if (i != k) {
                    questionPane.getChildren().add(badAnswersCheckBox[i]);
                } else {
                    questionPane.getChildren().add(goodAnswersCheckBox);
                }
            }

            if (k == 3) {
                questionPane.getChildren().add(goodAnswersCheckBox);
            } else {
                questionPane.getChildren().add(badAnswersCheckBox[k]);
            }
        }

        mediator.setView(questionPane); // Установка вопросов и ответов в посреднике для отображения на экране
    }

    /**
     /* Метод, который получает список вопросов и выполняет необходимую обработку.
     Здесь список вопросов перемешивается и выбираются первые NUMBER вопросов для отображения.
     Затем вызывается метод super.receive(current_test) для передачи списка вопросов родительскому классу Colleague.
     */
    public void receive(ArrayList<Question> message) {
        ArrayList<Question> current_test = new ArrayList<>(message);

        // Перемешиваем список вопросов
        Collections.shuffle(current_test);

        // Выбираем первые NUMBER вопросов
        current_test = new ArrayList<>(current_test.subList(0, Math.min(NUMBER, current_test.size())));

        super.receive(current_test); // Вызываем метод родительского класса для получения списка вопросов
    }
}
