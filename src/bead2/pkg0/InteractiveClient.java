package bead2.pkg0;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class InteractiveClient {

    final int PORT = 32123;
    Socket client;
    PrintWriter output;
    Scanner fromServer;
    Scanner keyInput;
    ArrayList<String> wordList;

    InteractiveClient() throws IOException {
        client = new Socket("localhost", PORT);
        output = new PrintWriter(client.getOutputStream(), true);
        fromServer = new Scanner(client.getInputStream());
        keyInput = new Scanner(System.in);
        wordList = new ArrayList<>();

        System.out.print("Please enter your name: ");
        output.println(keyInput.nextLine());

        new Thread() {
            @Override
            public void run() {
                while (handleStuff()) {}
            }
        }.start();
    }

    public boolean validator(String word, String serverInput) {
        if(word.length() == 0) {
            return false;
        }
        
        boolean valid = false;
        if (word.charAt(0) == serverInput.charAt(serverInput.length() - 1) &&
            word.matches("[a-z]+") && !(wordList.contains(word))) {
            valid = true;
        }
        return valid;
    }

    public boolean handleStuff() {
        boolean workStatus = true;
        String toBeSent;

        String msg = fromServer.nextLine();
        System.out.println("from server: " + msg);
        if ("nyert".equals(msg)) {
            workStatus = false;
        } else {

            System.out.print("Type word: ");
            toBeSent = keyInput.nextLine();
            if (!("start").equals(msg) && !("exit").equals(toBeSent)) {

                while (!validator(toBeSent, msg)) {
                    System.out.print("Type valid word: ");
                    toBeSent = keyInput.nextLine();
                    if("exit".equals(toBeSent)) {
                        output.println(toBeSent);
                        System.out.println("I give up");
                        break;
                    }
                }
            }
            wordList.add(toBeSent);
            output.println(toBeSent);

            if ("exit".equals(toBeSent)) {
                output.println(toBeSent);
                workStatus = false;
            }
        }
        return workStatus;
    }

    public static void main(String[] args) {
        try {
            new InteractiveClient();        
        } catch(IOException e) {
            System.out.println("Interactive client could not start");
        }
    }
}
