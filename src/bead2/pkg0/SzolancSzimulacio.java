package bead2.pkg0;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SzolancSzimulacio {

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                new Server();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos1", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Client 1 could not start");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos2", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Client 2 could not start");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos3", "szokincs1.txt");
                } catch (IOException ex) {
                    System.out.println("Client 3 could not start");
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    new RobotClient("Jatekos4", "szokincs2.txt");
                } catch (IOException ex) {
                    System.out.println("Client 4 could not start");
                }
            }
        }.start();
    }
}
