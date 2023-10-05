package com.example.denno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("myPharmacy");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch();
    }
}