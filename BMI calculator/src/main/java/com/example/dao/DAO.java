package com.example.dao;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DAO {
    void createTableIfNotExists() throws SQLException;
    ResultSet getDiagnosisMinMax(String sex, int age, double bmi) throws SQLException;
}
