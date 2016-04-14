package bead2.pkg0;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RobotClient {

    final int PORT = 32123;
    Socket client;
    PrintWriter output;
    Scanner fromServer;
    List<String> wordList;
    String name;

    RobotClient(String name, String file) throws IOException {
        this.name = name;

        client = new Socket("localhost", PORT);
        output = new PrintWriter(client.getOutputStream(), true);
        fromServer = new Scanner(client.getInputStream());
        wordList = readFile(file);

        output.println(name);

        new Thread() {
            @Override
            public void run() {
                while (handleStuff()) {}
            }
        }.start();
    }

    public List<String> readFile(String filename) {
        List<String> ls = new ArrayList<>();
        try (Reader reader = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            while (br.ready()) {
                line = br.readLine();
                ls.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No such file " + filename);
        } catch (IOException ex) {
            System.out.println("Something went wrong while operating with file");
        }
        return ls;
    }

    public boolean handleStuff() {
        boolean workStatus = true;

        String msg = fromServer.nextLine();
        
        if ("start".equals(msg)) {
            output.println(wordList.get(0));
            System.out.println(name + ": " + wordList.get(0));
            wordList.remove(0);
            
        } else if ("nyert".equals(msg)) {
            System.out.println(name + " " + msg);
            workStatus = false;
            
        } else {
            String toBeSent = "exit";
            workStatus = false;
            String lastLetter = msg.substring(msg.length() - 1);
            
            for(int i = 0; i < wordList.size(); ++i) {
                String firstLetter = wordList.get(i).substring(0,1);
                if(lastLetter.equals(firstLetter)) {
                    toBeSent = wordList.get(i);
                    wordList.remove(i);
                    workStatus = true;
                    break;
                }
            }
            output.println(toBeSent);
            System.out.println(name + ": " + toBeSent);
        }
        return workStatus;
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("invalid number of arguments");
            return;
        }
        try {
            new RobotClient(args[0], args[1]);
        }catch(IOException e) {
            System.out.println("Robot client could not start");
        }
    }
}
