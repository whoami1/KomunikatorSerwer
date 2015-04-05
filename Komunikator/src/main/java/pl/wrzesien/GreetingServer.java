package pl.wrzesien;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GreetingServer extends Thread {
    private ServerSocket serverSocket;

    public GreetingServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    boolean check(String l, String p)
    {
        //hibernateutils zmienic, żeby sprawdzało z bazy danych, używając podobnie jak w main
        return l.equals("test") && p.equals("pass");
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Oczekiwanie na klienta na porcie " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("Połączono z "
                        + server.getRemoteSocketAddress());
                DataInputStream in =
                        new DataInputStream(server.getInputStream());
                //System.out.println(in.readUTF());
                String[] split = in.readUTF().split(";");
                Boolean check = check(split[0], split[1]);
                DataOutputStream out =
                        new DataOutputStream(server.getOutputStream());
                out.writeUTF(check.toString());
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try {
            Thread t = new GreetingServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}