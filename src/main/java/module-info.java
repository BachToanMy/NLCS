module com.example.denno {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;



    opens com.example.denno to javafx.fxml;
    exports com.example.denno;
}