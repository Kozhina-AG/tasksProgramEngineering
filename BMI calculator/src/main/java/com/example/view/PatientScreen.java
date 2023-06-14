package com.example.view;

import com.example.Main;
import com.example.dao.PatientBMIDAO;
import com.example.dao.PatientDao;
import com.example.model.ObesityCategoriesSingleton;
import com.example.model.Patient;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Класс PatientsScreen отвечает за интерфейс для списка пациентов.
 * Он содержит методы для удаления пациента из базы данных, добавления расчетов в историю пациента,
 * поиск пациента по его СНИЛС и отображения графика ИМТ пациента.
 */

public class PatientScreen {
    private final BorderPane root;
    private final PatientDao patientDao;
    private final Main main;
    private TableView<Patient> patientTable;
    private TextField searchField;
    private Patient selectedPatient;

    /**
     * Конструктор класса PatientScreen.
     *
     * @param main экземпляр класса Main
     * @throws IOException если возникла ошибка ввода-вывода
     */

    public PatientScreen(Main main) throws IOException {
        this.main = main;
        this.root = new BorderPane();
        this.patientDao = new PatientDao();
        ObesityCategoriesSingleton obesityCategories = ObesityCategoriesSingleton.getInstance();
        initUI();
//        initBmiChart();
    }
    /**
     * Инициализирует пользовательский интерфейс.
     */
    private void initUI() {
        Label titleLabel = new Label("Список пациентов");
        titleLabel.setStyle("-fx-alignment: CENTER; -fx-font-size: 34; -fx-text-fill: linear-gradient(to right, #FFA500, #FF8C00); -fx-font-weight: bold;");

        // Создаем поле для ввода СНИЛСа и кнопку поиска
        searchField = new TextField();
        searchField.setPromptText("Введите СНИЛС:");
        Button searchButton = new Button("Поиск");
        searchButton.setOnAction(event -> searchPatients());

        HBox searchBox = new HBox(10, searchButton, searchField);

        // Создаем таблицу для отображения пациентов
        patientTable = new TableView<>();
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Patient, String> nameColumn = new TableColumn<>("Имя");

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Patient, String> snilsColumn = new TableColumn<>("СНИЛС");

        snilsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSnils()));

        TableColumn<Patient, Double> weightColumn = new TableColumn<>("Вес");

        weightColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getWeight()).asObject());

        TableColumn<Patient, Double> heightColumn = new TableColumn<>("Рост");

        heightColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getHeight()).asObject());

        TableColumn<Patient, Integer> ageColumn = new TableColumn<>("Возраст");

        ageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAge()).asObject());

        TableColumn<Patient, String> sexColumn = new TableColumn<>("Пол");

        sexColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSex()));
        patientTable.getColumns().addAll(nameColumn, snilsColumn, weightColumn, heightColumn, ageColumn, sexColumn);
        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldPatient, newPatient) -> {
            selectedPatient = newPatient;
            updateSelectedPatient();
        });
        patientTable.setStyle("-fx-alignment: CENTER; -fx-border-color: orange; -fx-border-width: 2;");

        // Создаем кнопки для удаления пациента и отображения графика ИМТ
        Button deleteButton = new Button("Удалить пациента");

        deleteButton.setOnAction(event -> {
            try {
                deleteSelectedPatient();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button showBmiChartButton = new Button("Показать график ИМТ");

        showBmiChartButton.setStyle("-fx-background-color: orange;");
        showBmiChartButton.setOnAction(event -> {
            try {
                showBmiChart();
            } catch (SQLException | ParseException e) {
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

        HBox buttonsBox = new HBox(10, deleteButton, showBmiChartButton, backButton);

        buttonsBox.setAlignment(Pos.CENTER);

        VBox topBox = new VBox(10, titleLabel, searchBox, patientTable, buttonsBox);

        topBox.setAlignment(Pos.CENTER);
        VBox.setMargin(patientTable, new Insets(10, 0, 0, 0));
        VBox.setMargin(searchBox, new Insets(0, 0, 10, 0));
        root.setCenter(topBox);
    }
    /**
     * Возвращает пользовательский интерфейс.
     *
     * @return объект BorderPane, представляющий пользовательский интерфейс
     */
    public BorderPane getUI() {
        return root;
    }

    /**
     * Выполняет поиск пациентов по СНИЛСу.
     */

    private void searchPatients() {
        String snils = searchField.getText();
        List<Patient> patients = main.getDataSource().equals("база") ? patientDao.searchPatientsInDB(snils) : patientDao.searchPatientsInFile(snils);
        ObservableList<Patient> patientsObservableList = FXCollections.observableArrayList(patients);

        patientTable.setItems(patientsObservableList);
    }

    /**
     * Удаляет выбранного пациента из базы данных или файла.
     *
     * @throws IOException если возникла ошибка ввода-вывода
     */

    private void deleteSelectedPatient() throws IOException {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();

        if (selectedPatient != null) {
            if ("база".equals(main.getDataSource())) patientDao.removePatientFromDB(selectedPatient);
            else patientDao.removePatientFromFile(selectedPatient);

            patientTable.getItems().remove(selectedPatient);
        }
    }
    /**
     * Обновляет информацию о выбранном пациенте.
     */
    private void updateSelectedPatient() {
        if (selectedPatient != null) {
            // Обновляем информацию о выбранном пациенте
            double height = selectedPatient.getHeight();
            double weight = selectedPatient.getWeight();
            int age = selectedPatient.getAge();
            String sex = selectedPatient.getSex();
            double bmi = weight / Math.pow(height / 100, 2);

            // Создаем поля для ввода данных
            Label weightLabel = new Label("Вес (кг):");
            TextField weightField = new TextField(String.valueOf(weight));
            Label heightLabel = new Label("Рост (см):");
            TextField heightField = new TextField(String.valueOf(height));
            Label ageLabel = new Label("Возраст (год):");
            TextField ageField = new TextField(String.valueOf(age));
            Label sexLabel = new Label("Пол:");
            ObservableList<String> sexes = FXCollections.observableArrayList("женщина", "мужчина");
            ComboBox<String> sexComboBox = new ComboBox<>(sexes);

            sexComboBox.getSelectionModel().select(String.valueOf(sex));

            // Создаем кнопку сохранения
            Button saveButton = new Button("Сохранить");
            saveButton.setOnAction(event -> {
                // Сохраняем новые значения веса и роста пациента
                double newWeight = Double.parseDouble(weightField.getText());
                double newHeight = Double.parseDouble(heightField.getText());
                int newAge = Integer.parseInt(ageField.getText());
                String newSex = String.valueOf(sexComboBox.getValue());

                if (newWeight != weight || newHeight != height) {
                    double newBMI = newWeight / Math.pow(newHeight / 100, 2);

                    new PatientBMIDAO().addToDB(selectedPatient.getId(), newBMI);
                }

                selectedPatient.setWeight(newWeight);
                selectedPatient.setHeight(newHeight);
                selectedPatient.setAge(newAge);
                selectedPatient.setSex(newSex);

                if ("база".equals(main.getDataSource()))
                    patientDao.updatePatientInDB(selectedPatient);
                else {
                    try {
                        patientDao.updatePatientInFile(selectedPatient, bmi);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Обновляем таблицу
                patientTable.refresh();
            });

            // Создаем контейнер для полей ввода и кнопки сохранения
            HBox inputBox = new HBox(10, weightLabel, weightField, heightLabel, heightField, ageLabel, ageField, sexLabel, sexComboBox, saveButton);

            inputBox.setAlignment(Pos.CENTER);
            root.setBottom(inputBox);
        }
    }

    /**
     * Отображает график ИМТ выбранного пациента.
     *
     * @throws SQLException   если возникла ошибка SQL
     * @throws ParseException если возникла ошибка парсинга
     */

    private void showBmiChart() throws SQLException, ParseException {
        if (selectedPatient == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Пожалуйста, выберите пациента из таблицы");

            alert.showAndWait();

            return;
        }

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Время");
        yAxis.setLabel("ИМТ");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2100").getTime());
        xAxis.setUpperBound(0);
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override
            public String toString(Number object) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

                return sdf.format(new Date(object.longValue()));
            }
        });

        LineChart<Number, Number> bmiChart = new LineChart<>(xAxis, yAxis);

        bmiChart.setTitle("График ИМТ");
        XYChart.Series<Number, Number> bmiSeries = new XYChart.Series<>();

        for (Map.Entry<Date, Double> entry : new PatientBMIDAO().getPatientBMIs(selectedPatient.getId()).entrySet()) {
            bmiSeries.getData().add(new XYChart.Data<>(entry.getKey().getTime(), entry.getValue()));

            if (entry.getKey().getTime() < xAxis.getLowerBound()) xAxis.setLowerBound(entry.getKey().getTime());

            if (entry.getKey().getTime() > xAxis.getUpperBound()) xAxis.setUpperBound(entry.getKey().getTime());
        }

        xAxis.setLowerBound(xAxis.getLowerBound() - 100000);
        xAxis.setUpperBound(xAxis.getUpperBound() + 100000);
        bmiSeries.setName("ИМТ");
        bmiChart.getData().add(bmiSeries);

        Stage stage = new Stage();
        Scene scene = new Scene(bmiChart, 600, 400);

        stage.setScene(scene);
        stage.show();
    }
    /**
     * Возвращает назад к главному экрану.
     *
     * @throws IOException если возникла ошибка ввода-вывода
     */
    private void goBack() throws IOException {
        main.initUI();
    }
}
