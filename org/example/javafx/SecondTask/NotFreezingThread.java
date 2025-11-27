package org.example.javafx.SecondTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// Пункт b. При нажатии на кнопку бесконечный цикл запускается в отдельном потоке и не блокирует работу приложения.
// Используется наследование от Thread.

public class NotFreezingThread extends Application {

    static class EndlessThread extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println("Работа бесконечного цикла в отдельном потоке");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start(Stage stage) {

        Button button = new Button("Запуск");

        button.setOnAction(e -> {

            EndlessThread thread = new EndlessThread();

            thread.setDaemon(true);
            thread.start();

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