package model;

import java.util.ArrayList;

public abstract class Colleague {
    protected Mediator mediator; // ссылка на объект посредника (Mediator), используемого для общения между ролями.
    protected ArrayList<Question> receivedMessage; // список сообщений (вопросов), полученных данным пользователем.

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
        this.receivedMessage = new ArrayList<>();
    }

    /**
     /* Абстрактный метод, который будет реализован в дочерних классах.
     Определяет, как роль будет обрабатывать полученные сообщения ль других пользователей.
     */
    public abstract void notifyColleague(ArrayList<Question> message);

    /**
     /* Метод, который устанавливает полученные сообщения в свойство receivedMessage.
     */
    public void receive(ArrayList<Question> message) {
        this.receivedMessage = message;
    }

    /**
     /* Метод, который возвращает список полученных сообщений.
     */
    public ArrayList<Question> getReceivedMessage() {
        return receivedMessage;
    }
}
