package bead2.pkg0;

import java.util.*;
import java.net.*;
import java.io.*;
import java.text.*;

public class Server {

    final private int port = 32123;
    ServerSocket server;

    public Server() {
        try {
            this.server = new ServerSocket(port);
            System.out.println("SERVER>> Server started on port " + port);
            server.setSoTimeout(30000);
            handleClients();
        } catch (IOException e) {
            System.out.println("SERVER>> Server stopped working beacause of idling");
        }
    }

    final void handleClients() throws IOException {
        while (true) {
            
            Socket s1 = server.accept();
            //System.out.println("Player1 connected");
            Socket s2 = server.accept();
            //System.out.println("Player2 connected");
            new Handler(s1, s2).start();
        }
    }

    public class Handler extends Thread {

        Socket player1, player2;
        String name1, name2;
        Scanner input1, input2;
        PrintWriter output1, output2;
        boolean isPlayer1;
        File fileName;
        FileWriter fw;

        public final void createFileName(String player1, String player2) throws IOException {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH-mm-ss");
            String formatedDate = sdf.format(date);

            String filename = player1 + "_" + player2 + "_" + formatedDate + ".txt";
            this.fileName = new File(filename);
            fw = new FileWriter(fileName);
        }

        public void writeToFile(String msg) throws IOException {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.write("\n");
            bw.flush();
        }

        Handler(Socket s1, Socket s2) throws IOException {
            this.player1 = s1;
            this.player2 = s2;
            input1 = new Scanner(s1.getInputStream());
            input2 = new Scanner(s2.getInputStream());

            output1 = new PrintWriter(s1.getOutputStream(), true);
            output2 = new PrintWriter(s2.getOutputStream(), true);

            name1 = input1.nextLine();
            name2 = input2.nextLine();
            
            System.out.println("SERVER>> " + name1 + " connected");
            System.out.println("SERVER>> " + name2 + " connected");
            
            isPlayer1 = true;
            createFileName(name1, name2);

        }

        @Override
        public void run() {
            String msg;
            output1.println("start");
            while (true) {
                if (isPlayer1) {
                    msg = input1.nextLine();
                    if (msg.equals("exit")) {
                        System.out.println("SERVER>> " + name2 + " nyert");
                        output2.println("nyert");
                        break;
                    }
                    output2.println(msg);
                    isPlayer1 = false;
                } else {
                    msg = input2.nextLine();
                    if (msg.equals("exit")) {
                        System.out.println("SERVER>> " + name1 + " nyert");
                        output1.println("nyert");
                        break;
                    }
                    output1.println(msg);
                    isPlayer1 = true;
                }
                try {
                    String s = (!isPlayer1) ? name1 + " " + msg : name2 + " " + msg;
                    writeToFile(s);
                } catch (IOException ex) {
                    System.out.println("SERVER>> File writing error");
                }
            }
            try {
                player1.close();
                System.out.println("SERVER>> " + name1 + " left");
                player2.close();
                System.out.println("SERVER>> " + name2 + " left");
            } catch (IOException e) {
                System.out.println("SERVER>> Socket closing error");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
