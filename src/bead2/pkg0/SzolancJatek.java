package bead2.pkg0;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancJatek {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                new Server();
            }
        }.start();
        synchronized (SzolancJatek.class) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        new RobotClient("Robot", "szokincs1.txt");
                    } catch (IOException ex) {
                        System.out.println("Robot client could not start");
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    try {
                        new InteractiveClient();
                    } catch (IOException ex) {
                        System.out.println("player client could not start");
                    }
                }
            }.start();
        }
    }
}
