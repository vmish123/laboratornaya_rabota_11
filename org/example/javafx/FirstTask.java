package org.example.javafx;

public class FirstTask {
    static final Object monitor = new Object();
    static boolean firstTurn = true;

    public static void main(String[] args) {

        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (monitor) {
                        while (!firstTurn) {
                            try {
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Поток1");
                        firstTurn = false;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        monitor.notify();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    synchronized (monitor) {
                        while (firstTurn) {
                            try {
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Поток2");
                        firstTurn = true;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        monitor.notify();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}