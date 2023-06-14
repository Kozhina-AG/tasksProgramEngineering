package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Question {
    private StringProperty question;
    private ArrayList<StringProperty> answerGood;
    private ArrayList<StringProperty> badAnswer;


    public Question(String question) {
        this.question = new SimpleStringProperty(question);
        this.answerGood = new ArrayList<>();
        this.badAnswer = new ArrayList<>();
    }

    /**
     /* Добавление нового правильного ответа в список правильных вопросов теста.
     Возвращение количества правильных ответов.
     */
    public int addTrue(String answer) {
        answerGood.add(new SimpleStringProperty(answer));
        return answerGood.size();
    }

    /**
     /* Добавление нового неправильного ответа в список неправильных вопросов теста.
     Возвращение количества неправильных ответов.
     */
    public int addFalse(String answer) {
        badAnswer.add(new SimpleStringProperty(answer));
        return badAnswer.size();
    }

    /**
     /* Установка нового значения для вопроса
     */
    public void setQuestion(String question) {
        this.question.set(question);
    }

    public ArrayList<StringProperty> getAnswerGood() {
        return answerGood;
    }

    public ArrayList<StringProperty> getBadAnswer() {
        return badAnswer;
    }

    public String getQuestion() {
        return question.get();
    }

    public StringProperty questionProperty() {
        return question;
    }
}
