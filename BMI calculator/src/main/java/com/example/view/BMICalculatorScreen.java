package com.example.view;

import com.example.Main;
import com.example.dao.*;
import com.example.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


/**
 * Класс BmiCalculatorScreen содержит интерфейс для калькулятора BMI.
 * Он содержит текстовые поля для ФИО пациента, СНИЛС пациента, Рост пациента, Вес пациента и кнопку "Рассчитать".
 * Он также содержит методы для добавления нового пациента в базу данных и вычисления ИМТ пациента.
 */

public class BMICalculatorScreen {
    private final Main main;
    private TextField nameField;
    private TextField snilsField;
    private TextField heightField;
    private TextField weightField;
    private TextField ageField;
    private ComboBox<String> sexComboBox;
    private Label resultLabel;

    /**
     * Конструктор класса BMICalculatorScreen.
     *
     * @param main экземпляр главного класса приложения
     */

    public BMICalculatorScreen(Main main) {
        this.main = main;
    }

    /**
     * Возвращает интерфейс калькулятора BMI.
     *
     * @return интерфейс калькулятора BMI
     */

    public VBox getUI() {
        Label titleLabel = new Label("BMI калькулятор");

        titleLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: linear-gradient(to right, #33cc33, #66ff66);");

        Label nameLabel = new Label("ФИО пациента:");
        nameField = new TextField();

        nameField.setPromptText("Введите ФИО пациента");

        Label snilsLabel = new Label("СНИЛС пациента:");
        snilsField = new TextField();

        snilsField.setPromptText("Введите СНИЛС пациента");

        Label heightLabel = new Label("Рост пациента (в см):");
        heightField = new TextField();

        heightField.setPromptText("Введите рост пациента");

        Label weightLabel = new Label("Вес пациента (в кг):");
        weightField = new TextField();

        weightField.setPromptText("Введите вес пациента");

        Label ageLabel = new Label("Возраст пациента (в годах):");
        ageField = new TextField();

        ageField.setPromptText("Введите возраст пациента");

        Label sexLabel = new Label("Пол пациента:");
        ObservableList<String> sexes = FXCollections.observableArrayList("женщина", "мужчина");
        sexComboBox = new ComboBox<>(sexes);
        Button calculateButton = new Button("Рассчитать");

        calculateButton.setOnAction(event -> {
            try {
                calculateBMI();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        Button backButton = new Button("Назад");

        backButton.setOnAction(event -> {
            try {
                goBack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        resultLabel = new Label();
        GridPane inputGrid = new GridPane();

        inputGrid.setVgap(10);
        inputGrid.setHgap(5);
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.add(nameLabel, 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(snilsLabel, 0, 1);
        inputGrid.add(snilsField, 1, 1);
        inputGrid.add(heightLabel, 0, 2);
        inputGrid.add(heightField, 1, 2);
        inputGrid.add(weightLabel, 0, 3);
        inputGrid.add(weightField, 1, 3);
        inputGrid.add(ageLabel, 0, 4);
        inputGrid.add(ageField, 1, 4);
        inputGrid.add(sexLabel, 0, 5);
        inputGrid.add(sexComboBox, 1, 5);

        HBox buttonBox = new HBox(10, calculateButton, backButton);

        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, titleLabel, inputGrid, buttonBox, resultLabel);

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/bmicalculator.css")).toExternalForm());

        return root;
    }
    /**
     * Вычисляет ИМТ пациента на основе введенных данных и выводит результат на экран.
     *
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    private void calculateBMI() throws SQLException {
        try {
            String name = nameField.getText().trim();
            String snils = snilsField.getText().trim();
            double height = Double.parseDouble(heightField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String sex = sexComboBox.getValue();
            double bmi = weight / Math.pow(height / 100, 2);
            ResultSet diagnosisMinMax = defineCategory(age, sex, bmi);
            String category = diagnosisMinMax.getString("diagnosis");
            String weightRange = String.format("%.1f - %.1f", diagnosisMinMax.getDouble("min"), diagnosisMinMax.getDouble("max"));
            VBox resultBox = new VBox();

            resultBox.getStyleClass().add("result-box");

            Label nameLabel = new Label("Имя пациента: " + name);
            Label snilsLabel = new Label("СНИЛС: " + snils);
            Label bmiLabel = new Label("Индекс массы тела: " + String.format("%.1f", bmi));
            Label categoryLabel = new Label("Категория: " + category);
            Label weightRangeLabel = new Label("Диапазон нормального веса: " + weightRange);

            resultBox.getChildren().addAll(nameLabel, snilsLabel, bmiLabel, categoryLabel, weightRangeLabel);
            resultLabel.setText("");
            resultLabel.setGraphic(resultBox);

            Patient patient = new Patient(name, snils, weight, height, age, sex);
            PatientDao patientDao = new PatientDao();
            PatientBMIDAO patientBMIDAO = new PatientBMIDAO();

            if ("база".equals(main.getDataSource())) {
                int patientId = patientDao.addToDBAndRetrieveId(patient);

                patientBMIDAO.addToDB(patientId, bmi);
            } else patientDao.addPatientToFile(patient, bmi);
        } catch (NumberFormatException e) {
            resultLabel.setText("Проверьте правильность введенных данных");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Переходит на предыдущий экран.
     *
     * @throws IOException если произошла ошибка при загрузке предыдущего экрана
     */

    private void goBack() throws IOException {
        main.initUI();
    }
    /**
     * Определяет категорию пациента на основе возраста, пола и ИМТ.
     *
     * @param age возраст пациента
     * @param sex пол пациента
     * @param bmi ИМТ пациента
     * @return результат запроса к базе данных с информацией о категории
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    private ResultSet defineCategory(int age, String sex, double bmi) throws SQLException {
        BMIMetrics bmiMetrics = new BMIMetrics();

        return bmiMetrics.getDiagnosisMinMax(sex, age,bmi);
    }
}
