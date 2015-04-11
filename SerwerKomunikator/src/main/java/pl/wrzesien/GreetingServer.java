package pl.wrzesien;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class GreetingServer extends Thread {
    private ServerSocket serverSocket;
    private Date time;

    public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    private void log(String text)
    {
        //okienko.wpis(System.currentTimeMillis() + "|" + text + "\n");
        time = new Date();
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println(df.format(time) + "|" + serverSocket.getLocalPort() + "|" + text);
    }

    public void run() {
        while (true) {
            try {
                log("Oczekiwanie na klienta na porcie " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();//nowy watek + przeslanie do niego tego socketa + kontynuacja petli
                new Thread(new SocketThread(server)).start();
            } catch (SocketTimeoutException s) {
                log("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = 6066;//Integer.parseInt(args[0]);
        try {
            Thread t = new GreetingServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}