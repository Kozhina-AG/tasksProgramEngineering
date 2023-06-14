package com.example.demo;

import com.example.demo.model.Dock;
import com.example.demo.model.Port;
import com.example.demo.model.Ship;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class HelloController implements Initializable {
    private static final int NUM_DOCS = 4; //пристани
    private static final int NUM_SHIPS = 10; //корабли
    private ExecutorService executorService;
    private BlockingQueue<Dock> dockQueue; //очередь - общий ресурс

    public VBox mainVBox;
    private VBox shipBox;
    private Label portStatusLabel;
    private Button startButton, stopButton;

    /**
    /* Запускает симуляцию работы порта.
     */
    private void startSimulation() {
        executorService = Executors.newFixedThreadPool(NUM_SHIPS); //создание пулла потоков

        for (int i = 0; i < NUM_SHIPS; i++) { //добавление каждого корабля в пул потоков в качестве задачи
            Ship ship = (Ship) shipBox.getChildren().get(i).getUserData();
            executorService.execute(ship);
        }

        portStatusLabel.setText("Порт работает");

        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    /**
     /* Останавливает симуляцию работы порта.
     */
    private void stopSimulation() { //выключение пулла потоков
        if (executorService != null) {
            executorService.shutdownNow();
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS); //завершение задач
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        dockQueue.clear();
        portStatusLabel.setText("Работа порта остановлена");

        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    /**
     * @param url URL-адрес корневого объекта или null, если нет доступа к нему.
     * @param resourceBundle ResourceBundle, связанный с корневым объектом или null.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dockQueue = new ArrayBlockingQueue<>(NUM_DOCS); //создание очереди
        Port port = new Port(dockQueue, NUM_DOCS); //создание объекта порта (поставщика)

        BackgroundImage myBI= new BackgroundImage(new Image("Порт.png",550,550,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        mainVBox.setBackground(new Background(myBI));

        shipBox = new VBox(10); //создание лейблов с кораблями
        shipBox.setAlignment(Pos.CENTER);
        for (int i = 1; i <= NUM_SHIPS; i++) {
            Ship ship = new Ship(i, dockQueue, port);
            shipBox.getChildren().add(ship.getShipLabel());
        }
        VBox.setMargin(shipBox, new Insets(0, 0, 30, 0));

        Thread portThread = new Thread(port); //запуск потока порта (производителя)
        portThread.start();

        portStatusLabel = new Label("Порт пуст");
        VBox.setMargin(portStatusLabel, new Insets(10, 0, 10, 0));
        portStatusLabel.setTextFill(Color.rgb(29, 53,87));
        portStatusLabel.setFont(new Font(28));

        startButton = new Button("Начать работу");
        stopButton = new Button("Остановить работу");

        HBox buttonContainer = new HBox(startButton, stopButton);
        buttonContainer.setSpacing(30);

        startButton.setOnAction(e -> startSimulation());
        stopButton.setOnAction(e -> stopSimulation());

        Separator separator = new Separator();
        separator.setStyle("-fx-background: #1D3557;");
        separator.setPrefHeight(4);

        buttonContainer.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(portStatusLabel, shipBox, separator, buttonContainer);
    }
}