module com.example.bmi {
    requires javafx.controls;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires com.h2database;
    requires org.testng;

    opens com.example.view to javafx.fxml;
    exports com.example;
}