module com.example.database_project_javafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.database_project_javafx to javafx.fxml;
    exports com.example.database_project_javafx;
}