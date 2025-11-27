package org.example.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ThirdTask extends Application {

    ProgressBar progressBar;
    Button buttonStart;
    Button buttonStop;
    Button buttonPause;
    ProgressThread thread2;
    Thread thread1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);

        buttonStart = new Button("Старт");
        buttonStop = new Button("Стоп");
        buttonPause = new Button("Пауза");

        buttonPause.setDisable(true);
        buttonStop.setDisable(true);

        buttonStart.setOnAction(event -> startProcess());
        buttonStop.setOnAction(event -> stopProcess());
        buttonPause.setOnAction(event -> pauseProcess());

        HBox pane = new HBox(10, buttonStart, buttonPause, buttonStop);
        pane.setAlignment(Pos.CENTER);

        VBox mainPane = new VBox(20, progressBar, pane);
        mainPane.setPadding(new Insets(20));

        Scene scene = new Scene(mainPane, 400, 150);
        stage.setScene(scene);
        stage.show();
    }

    private void startProcess() {

        stopProcess();

        thread2 = new ProgressThread(progressBar);

        thread1 = new Thread(thread2);
        thread1.setDaemon(true);
        thread1.start();

        buttonPause.setDisable(false);
        buttonStop.setDisable(false);
        buttonPause.setText("Пауза");
    }

    private void pauseProcess() {

        if (thread2 == null) return;

        if (!thread2.isPaused()) {
            thread2.pause();
            buttonPause.setText("Продолжить");
        } else {
            thread2.resume();
            buttonPause.setText("Пауза");
        }
    }

    private void stopProcess() {

        if (thread2 != null) {
            thread2.stop();
        }

        progressBar.setProgress(0);

        buttonPause.setText("Пауза");
        buttonPause.setDisable(true);
        buttonStop.setDisable(true);
        thread2 = null;
    }

    private static class ProgressThread implements Runnable {

        private final ProgressBar progressBar;

        private boolean running = true;
        private boolean paused = false;

        private final Object monitor = new Object();

        public ProgressThread(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for (int i = 0 ; i < 1000 ; i++) {

                if (!running) break;

                synchronized (monitor) {
                    while (paused) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                if (!running) break;

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                double progress = i / 1000.0;
                Platform.runLater(() -> {
                    if (running) progressBar.setProgress(progress);
                });
            }
        }

        public void stop () {
            running = false;
            resume();
        }

        public void pause () {
            paused = true;
        }

        public boolean isPaused () {
            return paused;
        }

        public void resume () {
            synchronized (monitor) {
                paused = false;
                monitor.notify();
            }
        }
    }
}