import com.example.dao.PatientDao;
import com.example.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PatientDaoTest {

    @Test
    void searchPatientsInDB() {
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();

            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "12435", 45, 162, 21, "Female");

            // Добавляем пациента в базу данных
            patientDao.addToDBAndRetrieveId(patient);

            // Выполняем поиск пациентов в базе данных по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInDB("12435");

            // Проверяем, что список пациентов содержит тестового пациента
            Assertions.assertTrue(Objects.equals(patients.get(0).getName(), "Кожина А.Г."));

            // Удаляем тестового пациента из базы данных
            patientDao.removePatientFromDB(patient);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void removePatientFromDB() {
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();
            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "12345", 45, 162, 21, "Female");

            // Добавляем пациента в базу данных
            int id = patientDao.addToDBAndRetrieveId(patient);

            // Удаляем пациента из базы данных
            patientDao.removePatientFromDB(patient);

            // Выполняем поиск пациентов в базе данных по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInDB("12345");

            // Проверяем, что список пациентов не содержит удаленного пациента
            Assertions.assertFalse(patients.contains(patient));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addPatientToFile() { //Добавление пациента в файл
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();
            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "12534", 45, 162, 21, "Female");

            // Добавляем пациента в файл
            double bmi = calculateBMI(patient);
            patientDao.addPatientToFile(patient, bmi);

            // Выполняем поиск пациентов в файле по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInFile("12534");
            // Проверяем, что список пациентов содержит тестового пациента
            Assertions.assertTrue(Objects.equals(patients.get(0).getName(), "Кожина А.Г."));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchPatientsInFile() { // Поиск пациента в файле
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();
            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "94562", 45, 162, 21, "Female");

            // Добавляем пациента в файл
            double bmi = calculateBMI(patient);
            patientDao.addPatientToFile(patient, bmi);

            // Выполняем поиск пациентов в файле по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInFile("94562");

            // Проверяем, что список пациентов содержит тестового пациента
            Assertions.assertTrue(Objects.equals(patients.get(0).getName(), "Кожина А.Г."));

            // Удаляем пациента из файла
            patientDao.removePatientFromFile(patient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updatePatientInFile() {
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();
            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "19823", 45, 162, 21, "Female");

            // Добавляем пациента в файл
            double bmi = calculateBMI(patient);
            patientDao.addPatientToFile(patient, bmi);

            // Обновляем данные пациента
            patient.setName("Сыромятникова А.Ю");
            patient.setAge(35);

            // Выполняем обновление пациента в файле
            patientDao.updatePatientInFile(patient, bmi);

            // Выполняем поиск пациентов в файле по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInFile("19823");
            Patient updatedPatient = patients.get(0);

            // Проверяем, что данные пациента были успешно обновлены
            Assertions.assertEquals("Сыромятникова А.Ю", updatedPatient.getName());
            Assertions.assertEquals(35, updatedPatient.getAge());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void removePatientFromFile() {
        PatientDao patientDao; // Создаем объект PatientDao
        try {
            patientDao = new PatientDao();
            // Создаем тестового пациента
            Patient patient = new Patient("Кожина А.Г.", "12361", 45, 162, 21, "Female");

            // Добавляем пациента в файл
            double bmi = calculateBMI(patient);
            patientDao.addPatientToFile(patient, bmi);

            // Удаляем пациента из файла
            patientDao.removePatientFromFile(patient);

            // Выполняем поиск пациентов в файле по СНИЛСу
            List<Patient> patients = patientDao.searchPatientsInFile("12361");

            // Проверяем, что список пациентов не содержит удаленного пациента
            Assertions.assertFalse(patients.contains(patient));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateBMI(Patient patient) {
        double heightInMeters = patient.getHeight() / 100.0;
        return patient.getWeight() / (heightInMeters * heightInMeters);
    }

    /*
    Тесты выполняют следующие действия:

    searchPatientsInDB() - проверяет поиск пациентов в базе данных по СНИЛСу.

    removePatientFromDB() - проверяет удаление пациента из базы данных.

    addPatientToFile() - проверяет добавление пациента в файл.

    searchPatientsInFile() - проверяет поиск пациентов в файле по СНИЛСу.

    updatePatientInFile() - проверяет обновление данных пациента в файле.

    removePatientFromFile() - проверяет удаление пациента из файла.
    */
}