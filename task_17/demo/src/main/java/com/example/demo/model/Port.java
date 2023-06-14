package com.example.demo.model;

import java.util.concurrent.BlockingQueue;

public class Port implements Runnable {
    private BlockingQueue<Dock> dockQueue; //объект очереди
    private int NUM_DOCKS; //количество пристаней

    private boolean newDockNeeded = false; //нужно добавить новую пристань
    private boolean portWasBorn = true; //первоначальное заполнение очереди
    private Dock dock = null;

    public Port(BlockingQueue<Dock> dockQueue, int NUM_DOCKS) {
        this.dockQueue = dockQueue;
        this.NUM_DOCKS = NUM_DOCKS;
    }

    public void newDockNeeded(boolean newDockNeeded, Dock dock) {
        this.newDockNeeded = newDockNeeded;
        this.dock = dock;
    }

    /**
     /* Обработчик потока порта.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { //пока текущий поток не приостановлен

            if(portWasBorn){ //создан порт
                for (int i = 1; i <= NUM_DOCKS; i++) {
                    try {
                        dockQueue.put(new Dock(i)); //добавляет в очередь объект пристани
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                portWasBorn = false;
            }

           if(newDockNeeded){ //проверка того, что нужно добавить новую пристань в очередь
               try {
                   dockQueue.put(dock);
                   newDockNeeded = false;
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        }
    }

}