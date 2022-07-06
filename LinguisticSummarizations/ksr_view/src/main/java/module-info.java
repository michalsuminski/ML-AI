module com.example_view {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example;
    requires org.controlsfx.controls;
//    requires com.example;

    opens com.example_view to javafx.fxml;
    exports com.example_view;
}
