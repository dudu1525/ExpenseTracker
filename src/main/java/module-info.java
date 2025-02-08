module javafiles {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.sql;
    requires java.desktop;
    requires java.prefs;


    opens javafiles.controllers to javafx.fxml;
    opens javafiles to javafx.fxml;
    opens javafiles.fxmlcomponents to javafx.fxml;
    opens javafiles.managers to javafx.fxml;

    exports javafiles.managers;
    exports javafiles;
}