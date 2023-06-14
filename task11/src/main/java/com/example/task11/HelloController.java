package com.example.task11;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController implements Initializable, Mediator {
    public ScrollPane viewpane;

    public ChoiceBox choiceBox; // choiceBox для выбора роли
    private HashMap<String, Colleague> id = new HashMap<>(); // Создаем хэш-карту для хранения коллег по их идентификатору
    private Colleague currentcolleague;
    private Test test = new Test(); // Создаем экземпляр теста

    private ObservableList list = FXCollections.observableArrayList();

    private void createBox() {
        list.add("Студент");
        list.add("Преподаватель");
        list.add("Наблюдатель");

        choiceBox.getItems().addAll(list);
    }

    public void execute(ActionEvent actionEvent) {
        currentcolleague = id.get(String.valueOf(choiceBox.getValue()));

        if(currentcolleague == null){
            currentcolleague = id.get("Студент");
        }

        currentcolleague.receive(test.getTest());
        currentcolleague.notifyColleague(currentcolleague.getReceivedMessage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.put("Студент", new Student(this)); // Добавляем студента в хэш-карту по ключу "Студент"
        id.put("Преподаватель", new Tutor(this)); // Добавляем преподавателя в хэш-карту по ключу "Преподаватель"
        id.put("Наблюдатель", new Viewer(this)); // Добавляем наблюдателя в хэш-карту по ключу "Наблюдатель"

        createBox();
    }

    @Override
    public void setView(Node control) {
        Group root = new Group();
        ScrollBar sc = new ScrollBar(); // Создаём прокрутку

        // Настройка объекта прокрутки
        sc.setLayoutX(control.getLayoutX());
        control.setLayoutX(control.getLayoutX()+sc.getWidth());
        sc.setMin(0);
        sc.setValue(50);
        sc.setMax(100);

        sc.setOrientation(Orientation.VERTICAL); // Указываем, что прокрутка будет вертикальная

        root.getChildren().addAll(control,sc);
        viewpane.setContent(control);
    }
}
