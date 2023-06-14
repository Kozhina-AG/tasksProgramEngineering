package model;

import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Tutor extends Colleague {
    public Tutor(Mediator mediator) {
        super(mediator);
    }

    /**
     * Отрисовка вкладки роли преподавателя
     *
     * @param message Список вопросов для отображения
     */
    @Override
    public void notifyColleague(ArrayList<Question> message) {
        VBox questionPane = new VBox();

        for (int qi = 0; qi < message.size(); qi++) {
            TextField questionField = new TextField();
            questionField.textProperty().bindBidirectional(message.get(qi).questionProperty());
            questionPane.getChildren().add(questionField);

            Separator separator = new Separator();
            separator.setMaxWidth(20);
            questionPane.getChildren().add(separator);

            // Отображение правильных ответов
            for (int i = 0; i < message.get(qi).getAnswerGood().size(); i++) {
                TextField goodAnswerField = new TextField();
                goodAnswerField.textProperty().bindBidirectional(message.get(qi).getAnswerGood().get(i));
                questionPane.getChildren().add(goodAnswerField);
            }

            Separator separator2 = new Separator();
            separator2.setMaxWidth(40);
            questionPane.getChildren().add(separator2);

            // Отображение неправильных ответов
            for (int i = 0; i < message.get(qi).getBadAnswer().size(); i++) {
                TextField badAnswerField = new TextField();
                badAnswerField.textProperty().bindBidirectional(message.get(qi).getBadAnswer().get(i));
                questionPane.getChildren().add(badAnswerField);
            }
        }

        mediator.setView(questionPane);
    }
}
