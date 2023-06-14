package com.example.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**
 * Data Access Object (DAO) для работы с данными пациентов и их BMI (Body Mass Index).
 */
public class PatientBMIDAO{
    private static final String DB_URL = "jdbc:h2:~/patients";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";
    private static final String TABLE = "patient_bmi";
    private Connection connection;
    /**
     * Конструктор класса PatientBMIDAO.
     * Устанавливает соединение с базой данных и создает таблицу, если она не существует.
     */
    public PatientBMIDAO() {
        try {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            createTableIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Создает таблицу для хранения данных BMI, если она не существует.
     *
     * @throws SQLException если происходит ошибка SQL при создании таблицы.
     */
    public void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (id INT AUTO_INCREMENT PRIMARY KEY, patient_id INT NOT NULL REFERENCES patients (id) ON DELETE CASCADE, bmi_value DOUBLE NOT NULL, measured_at TIMESTAMP NOT NULL);";
        Statement statement = connection.createStatement();

        statement.executeUpdate(sql);
    }
    /**
     * Добавляет запись BMI пациента в базу данных.
     *
     * @param patientId идентификатор пациента
     */
    public void addToDB(int patientId, double bmi_value) {
        try {
            if (connection == null) throw new IllegalStateException("Connection has not been established");

            String sql = "INSERT INTO " + TABLE + " (patient_id, bmi_value, measured_at) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, patientId);
            preparedStatement.setDouble(2, bmi_value);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Возвращает карту, содержащую дату и значение BMI для всех записей пациента.
     *
     * @param patientId идентификатор пациента
     * @return карта, содержащая дату и значение BMI для каждой записи
     * @throws SQLException если происходит ошибка SQL при выполнении запроса.
     */
    public Map<java.util.Date, Double> getPatientBMIs(int patientId) throws SQLException {
        if (connection == null) throw new IllegalStateException("Connection has not been established");

        Map<java.util.Date, Double> patientBMIs = new HashMap<>();
        String sql = "SELECT bmi_value, measured_at FROM " + TABLE + " WHERE patient_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1, patientId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next())
            patientBMIs.put(resultSet.getTimestamp("measured_at"), resultSet.getDouble("bmi_value"));

        return patientBMIs;
    }
}
