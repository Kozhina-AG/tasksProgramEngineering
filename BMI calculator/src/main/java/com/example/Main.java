package com.example;

import com.example.dao.UserDAO;
import com.example.view.BMICalculatorScreen;
import com.example.view.PatientScreen;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import org.h2.engine.User;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private BMICalculatorScreen bmiCalculatorScreen;
    private PatientScreen patientScreen;
    private String dataSource;

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Возвращает источник данных, используемый в приложении.
     *
     * @return источник данных ("база" или "файл")
     */

    public String getDataSource() {
        return dataSource;
    }

    /**
     * Метод, вызываемый при запуске приложения.
     *
     * @param primaryStage главная сцена приложения
     * @throws IOException если произошла ошибка при загрузке пользовательского интерфейса
     */

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.bmiCalculatorScreen = new BMICalculatorScreen(this);
        this.patientScreen = new PatientScreen(this);
        dataSource = "база";
        initUI();
    }
    /**
     * Инициализирует главный пользовательский интерфейс приложения.
     *
     * @throws IOException если произошла ошибка при загрузке пользовательского интерфейса
     */
    public void initUI() throws IOException {
        // Создаем главный экран
        Label label = new Label("Медицинская база данных пациентов");
        label.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold; -fx-background-color: linear-gradient(to right, #0099ff, #33ccff); -fx-background-radius: 30;");

        Button bmiButton = new Button("Калькулятор ИМТ");
        bmiButton.setOnAction(event -> showBmiCalculatorScreen());
        bmiButton.setStyle("-fx-font-size: 14pt; -fx-pref-width: 250px;");

        Button patientsButton = new Button("Пациенты");
        patientsButton.setOnAction(event -> {
            try {
                showPatientsScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        patientsButton.setStyle("-fx-font-size: 14pt; -fx-pref-width: 250px;");

        Button changeDataSourceButton = new Button("Источник данных: база");
        changeDataSourceButton.setOnAction(actionEvent -> {
            if (changeDataSourceButton.getText().contains("база")) {
                changeDataSourceButton.setText("Источник данных: файл");

                dataSource = "файл";
            } else {
                changeDataSourceButton.setText("Источник данных: база");

                dataSource = "база";
            }
        });
        changeDataSourceButton.setStyle("-fx-font-size: 14pt; -fx-pref-width: 250px;");

        HBox buttonPanel = new HBox();
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setSpacing(20);
        buttonPanel.getChildren().addAll(bmiButton, patientsButton, changeDataSourceButton);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-padding: 20px 0;");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(label, separator, buttonPanel);
        root.setStyle("-fx-background-color: #F5F5F5; -fx-padding: 20px;");

        new UserDAO();

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Отображает экран калькулятора ИМТ.
     */

    private void showBmiCalculatorScreen() {
        Parent bmiCalculatorScreenUI = bmiCalculatorScreen.getUI();
        Scene scene = new Scene(bmiCalculatorScreenUI, 600, 300);
        primaryStage.setScene(scene);

    }

    /**
     * Отображает экран со списком пациентов.
     *
     * @throws IOException если произошла ошибка при загрузке пользовательского интерфейса
     */

    private void showPatientsScreen() throws IOException {
        if (patientScreen == null) {
            patientScreen = new PatientScreen(this);
        }
        Parent patientScreenUI = patientScreen.getUI();
        Scene scene = new Scene(patientScreenUI, 650, 650);
        primaryStage.setScene(scene);
    }
}
