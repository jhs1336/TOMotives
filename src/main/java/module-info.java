module com.tomotives.tomotives {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires org.apache.commons.text; // org.apache.commons:commons-text:1.13.0

    opens com.tomotives.tomotives to javafx.fxml;
    exports com.tomotives.tomotives;
    exports com.tomotives.tomotives.models;
    opens com.tomotives.tomotives.models to javafx.fxml;
    exports com.tomotives.tomotives.controllers;
    opens com.tomotives.tomotives.controllers to javafx.fxml;
    exports com.tomotives.tomotives.services;
    opens com.tomotives.tomotives.services to javafx.fxml;
}