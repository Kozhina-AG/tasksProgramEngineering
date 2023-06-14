package model;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextArea;
import java.util.ArrayList;

public class Viewer extends Colleague {

    private static final int NUMBER = 10;

    public Viewer(Mediator mediator) {
        super(mediator);
    }

    /**
     * Отрисовка вкладки
     *
     * @param test Список вопросов для отображения
     */
    @Override
    public void notifyColleague(ArrayList<Question> test) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < test.size(); i++) {
            str.append((i + 1)).append(" ").append(test.get(i).getQuestion()).append("\n\r");

            for (StringProperty answer : test.get(i).getAnswerGood()) {
                str.append(answer.get()).append("\n");
            }

            str.append("неправильные ответы").append("\n\r");
            for (StringProperty answer : test.get(i).getBadAnswer()) {
                str.append(answer.get()).append("\r\n");
            }
        }

        TextArea textArea = new TextArea(str.toString());
        textArea.setWrapText(true);
        mediator.setView(textArea);
    }

    /**
     * Прием вопросов и формирование теста для зрителя
     *
     * @param message Список вопросов для приема
     */
    public void receive(ArrayList<Question> message) {
        ArrayList<Question> current_test = new ArrayList<>(message);
        ArrayList<Question> test = new ArrayList<>(message);

        for (int i = 0; i < NUMBER && i < test.size(); i++) {
            int index = (int) (Math.random() * test.size()); // Случайный индекс для выбора вопроса из списка
            if (index == test.size()) {
                index--; // Проверка на выход за границы списка
            }

            current_test.add(test.get(index)); // Добавление выбранного вопроса в текущий тест
            test.remove(index); // Удаление выбранного вопроса из списка для исключения повторений
        }
        super.receive(current_test);
    }
}
