module de.leghast {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens de.leghast to javafx.fxml;

    exports de.leghast;
}