package com.example.dao;

import java.sql.*;

public class BMIMetrics implements DAO{
    private static final String DB_URL = "jdbc:h2:~/patients";
    private static final String DB_USERNAME = "sa";
    private static final String DB_PASSWORD = "";
    private static final String TABLE = "metric_table";
    private Connection connection;


    // Конструктор класса
    public BMIMetrics() {
        try {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            createTableIfNotExists();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Заполнение таблицы данными
    public void createTableIfNotExists() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "sex VARCHAR(255), " +
                    "age INT, " +
                    "min DOUBLE, " +
                    "max DOUBLE, " +
                    "diagnosis VARCHAR(255)" +
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            sql = "INSERT INTO " + TABLE + " (sex, age, min, max, diagnosis) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);

            // Заполнение данных в виде массива объектов
            Object[][] data = {
                    {"женщина", 18, 0, 17.5, "Значительный дефицит массы тела"},
                    {"женщина", 45, 0, 16, "Значительный дефицит массы тела"},
                    {"женщина", 65, 0, 16.8, "Значительный дефицит массы тела"},

                    {"женщина", 18, 16, 17.9, "Дефицит массы тела"},
                    {"женщина", 45, 16, 18.3, "Дефицит массы тела"},
                    {"женщина", 65, 16.8, 18.9, "Дефицит массы тела"},

                    {"женщина", 18, 17.9, 20.5, "Норма"},
                    {"женщина", 45, 18.3, 23.0, "Норма"},
                    {"женщина", 65, 18.9, 24.9, "Норма"},

                    {"женщина", 18, 20.5, 23.3, "Лишний вес"},
                    {"женщина", 45, 23.0, 27.0, "Лишний вес"},
                    {"женщина", 65, 24.9, 30, "Лишний вес"},


                    {"женщина", 18, 23.3, 25.7, "Ожирение первой степени"},
                    {"женщина", 45, 27.0, 31.0, "Ожирение первой степени"},
                    {"женщина", 65, 30.0, 34.0, "Ожирение первой степени"},

                    {"женщина", 18, 25.7, 32.0, "Ожирение второй степени"},
                    {"женщина", 45, 31.0, 38.0, "Ожирение второй степени"},
                    {"женщина", 65, 34.0, 40.0, "Ожирение второй степени"},

                    {"женщина", 18, 32.0, 100.0, "Ожирение третьей степени"},
                    {"женщина", 45, 38.0, 100.0, "Ожирение третьей степени"},
                    {"женщина", 65, 40.0, 100.0, "Ожирение третьей степени"},


                    {"мужчина", 18, 0, 17.5, "Значительный дефицит массы тела"},
                    {"мужчина", 45, 0, 16.0, "Значительный дефицит массы тела"},
                    {"мужчина", 65, 0, 16.8, "Значительный дефицит массы тела"},

                    {"мужчина", 18, 16.0, 17.9, "Дефицит массы тела"},
                    {"мужчина", 45, 16.0, 18.3, "Дефицит массы тела"},
                    {"мужчина", 65, 16.8, 18.9, "Дефицит массы тела"},

                    {"мужчина", 18, 17.9, 20.5, "Норма"},
                    {"мужчина", 45, 18.3, 23.0, "Норма"},
                    {"мужчина", 65, 18.9, 24.9, "Норма"},

                    {"мужчина", 18, 20.5, 23.3, "Лишний вес"},
                    {"мужчина", 45, 23, 27.0, "Лишний вес"},
                    {"мужчина", 65, 24.9, 30.0, "Лишний вес"},


                    {"мужчина", 18, 23.3, 25.7, "Ожирение первой степени"},
                    {"мужчина", 45, 27.0, 31.0, "Ожирение первой степени"},
                    {"мужчина", 65, 30.0, 34.0, "Ожирение первой степени"},

                    {"мужчина", 18, 25.7, 32.0, "Ожирение второй степени"},
                    {"мужчина", 45, 31, 38.0, "Ожирение второй степени"},
                    {"мужчина", 65, 34.0, 40.0, "Ожирение второй степени"},

                    {"мужчина", 18, 32.0, 100.0, "Ожирение третьей степени"},
                    {"мужчина", 45, 38.0, 100.0, "Ожирение третьей степени"},
                    {"мужчина", 65, 40.0, 100.0, "Ожирение третьей степени"}
            };

            for (Object[] row : data) {
                preparedStatement.setString(1, (String) row[0]);
                preparedStatement.setInt(2, (int) row[1]);
                preparedStatement.setDouble(3,Double.parseDouble(row[2].toString()));
                preparedStatement.setDouble(4, Double.parseDouble(row[3].toString()));
                preparedStatement.setString(5, (String) row[4]);

                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Получает диагноз и диапазон min/max для заданного значения BMI.
     *
     * @param sex пол (мужчина, женщина)
     * @param age    возраст
     * @param bmi    значение BMI
     * @return результат SQL-запроса с диагнозом и диапазоном min/max
     * @throws SQLException если происходит ошибка SQL при выполнении запроса.
     */
    public ResultSet getDiagnosisMinMax(String sex, int age, double bmi) throws SQLException {
        if(age <= 18){
            age = 18;
        }else if(age <= 45){
            age = 45;
        }else {
            age = 65;
        }

        String sql = "SELECT diagnosis, min, max FROM "  + TABLE + " WHERE sex = ? AND age = ? AND ? BETWEEN min AND max";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, sex);
        preparedStatement.setInt(2, age);
        preparedStatement.setDouble(3, bmi);

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();

        return resultSet;
    }
}