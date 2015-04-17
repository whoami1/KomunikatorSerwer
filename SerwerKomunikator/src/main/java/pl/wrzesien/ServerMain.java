package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.logback.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServerMain extends Thread {
    private ServerSocket serverSocket;
    private SimpleDateFormat timeAndDate;
    private Date time;

    private Message message = new Message();
    private static final Logger logger = LoggerFactory.getLogger(SocketThread.class);

    private ArrayList<String> userList = new ArrayList<>();

    public void addOnlineUser(String username) {
        userList.add(username);
    }

    public void removeOnlineUser(String username) {
        userList.remove(username);
    }

    public void printOnlineUsers() {
        System.out.println();
        message.logServerSocket(serverSocket, "Zalogowani uzytkownicy:");
        for (String u : userList) {
            System.out.println(u);
        }
        System.out.println();
    }

    public ServerMain(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

/*    private void log(String text) {
        //okienko.wpis(System.currentTimeMillis() + "|" + text + "\n");
        timeAndDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss (Z)");
        time = new Date();
        //DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println(timeAndDate.format(time) + "|" + serverSocket.getLocalPort() + "|" + text);
    }*/

    public void run() {
        while (true) {
            try {
                message.logServerSocket(serverSocket,"Oczekiwanie na klienta na porcie " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();//nowy watek + przeslanie do niego tego socketa + kontynuacja petli
                new Thread(new SocketThread(server, this)).start();
            } catch (SocketTimeoutException s) {
                message.logServerSocket(serverSocket,"Socket timed out!");
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
            Thread t = new ServerMain(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}