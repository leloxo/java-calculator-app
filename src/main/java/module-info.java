module com.github.leloxo.simplecalculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.github.leloxo.simplecalculator to javafx.fxml;
    exports com.github.leloxo.simplecalculator;
}