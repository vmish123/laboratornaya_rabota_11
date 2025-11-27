package org.example.javafx.SecondTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Пункт a. При нажатии на кнопку окно приложения перестает отвечать и зависает.

public class GuiFreezing extends Application {

    @Override
    public void start(Stage stage) {

        Button button = new Button("Запуск");

        button.setOnAction(e -> {
            while (true) {
                System.out.println("Работа бесконечного цикла в потоке с GUI");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        BorderPane pane = new BorderPane();
        pane.setCenter(button);
        Scene scene = new Scene(pane, 200, 200);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
