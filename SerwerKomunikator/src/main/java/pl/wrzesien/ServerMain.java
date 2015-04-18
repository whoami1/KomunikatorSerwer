package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.logback.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ServerMain extends Thread {
    private ServerSocket serverSocket;

    private Message message = new Message();
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    private ArrayList<User> userList = new ArrayList<>();

    public void addOnlineUser(User username) {
        userList.add(username);
    }

    public void removeOnlineUser(User username) {
        userList.remove(username);
    }

    public void printOnlineUsers() {
        System.out.println();
        message.logServerSocket(serverSocket, "Zalogowani uzytkownicy:");
        for (User u : userList)
        {
            if(userList.size() != 0)
            {
                System.out.println(u.getUserNick());
            }
            else
            {
                //z jakiegos powodu to nie dziala
                System.out.println("Brak");
                message.logTxt("Brak zalogowanych uzytkownikow");
            }
        }
        System.out.println();
    }

    public ServerMain(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

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
        int port = 6066;
        try {
            Thread t = new ServerMain(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}