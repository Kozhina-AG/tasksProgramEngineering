package com.example.demo.model;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Ship implements Runnable {
    private int shipNumber;
    private BlockingQueue<Dock> dockQueue;
    private Label shipLabel;
    private Port port;

    private boolean loaded = false;
    private boolean unloaded = true;

    public Ship(int shipNumber, BlockingQueue<Dock> dockQueue, Port port) {
        this.shipNumber = shipNumber;
        this.dockQueue = dockQueue;

        this.shipLabel = new Label("Корабль " + shipNumber + ": в плавании");
        this.shipLabel.setTextFill(Color.rgb(29, 53,87));
        this.shipLabel.setUserData(this);

        this.port = port;
    }

    /**
     /* Возвращает лейбл корабля.
     * @return лейбл корабля
     */
    public Label getShipLabel() {
        return shipLabel;
    }

    /**
     /* Обработчик потока коробля.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Dock dock = dockQueue.poll();

                if(dock == null){
                    updateLabel("В порту нет мест, корабль " + shipNumber + " уходит");
                    Thread.sleep(getRandomDelay());
                }else{
                    if (unloaded){ //корабль нужно загрузить
                        loading(dock);
                        leaving(dock);
                    }else if(loaded){ //корабль загружен
                        unloading(dock);
                        leaving(dock);
                    }

                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     /* Метод загрузки корабля на пристани.
     * @param dock пристань
     * @throws InterruptedException если поток прерван во время ожидания
     */
    private void loading(Dock dock) throws InterruptedException {
        updateLabel("Корабль " + shipNumber + ": прибыл на пристань №" + dock.getDockNumber());
        Thread.sleep(getRandomDelay());

        updateLabel("Корабль " + shipNumber + ": загружается на пристани №" + dock.getDockNumber());
        Thread.sleep(getRandomDelay());
    }

    /**
     /* Метод ухода коробля с пристани.
     * @param dock пристань
     * @throws InterruptedException если поток прерван во время ожидания
     */
    private void leaving(Dock dock) throws InterruptedException {
        updateLabel("Корабль " + shipNumber + ": ушёл с пристани №" + dock.getDockNumber());
        Thread.sleep(getRandomDelay());

        updateLabel("Корабль " + shipNumber + ": в плавании");
        Thread.sleep(getRandomDelay());

        loaded = true;
        unloaded = false;

        port.newDockNeeded(true, dock); //сообщаем, что нужно добавить пристань
    }

    /**
     /* Метод разгрузки корабля на пристани.
     * @param dock пристань
     * @throws InterruptedException если поток прерван во время ожидания
     */
    private void unloading(Dock dock) throws InterruptedException {
        updateLabel("Корабль " + shipNumber + ": прибыл на пристань №" + dock.getDockNumber());
        Thread.sleep(getRandomDelay());

        updateLabel("Корабль " + shipNumber + ": разгружается на пристани №" + dock.getDockNumber());
        Thread.sleep(getRandomDelay());

        loaded = false;
        unloaded = true;
    }

    /**
     /* Обновляет текст лейбла корабля.
     * @param text новый текст для лейбла
     */
    private void updateLabel(String text) {
        Platform.runLater(() -> shipLabel.setText(text));
    } //обновление лейбла

    /**
     /* Генерирует случайную задержку в миллисекундах.
     * @return случайная задержка
     */
    private long getRandomDelay() {
        return ThreadLocalRandom.current().nextInt(2000, 6000);
    }
}