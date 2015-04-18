package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entity.request.LoginRequest;
import pl.entity.request.RegisterRequest;
import pl.entity.request.TestowaWiadomoscRequest;
import pl.entity.response.LoginResponse;
import pl.entity.response.RegistrationResponse;
import pl.entity.response.TestowaWiadomoscResponse;
import pl.logback.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by Michał Wrzesień on 2015-03-31.
 */
public class Client {
    private String serverIp;
    private Socket client;
    //private OutputStream outToServer;
    //private InputStream inFromServer;
    //private DataOutputStream out;
    //private DataInputStream in;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Message message = new Message();
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public Client() {
    }

    public boolean connect(String serverIp) {
        //proces czyli czy logowanie czy rejestracja
        int port = 6066;/*Integer.parseInt(args[1]);*/
        this.serverIp = serverIp;

        try {
            client = new Socket(serverIp, port);
            message.log(client,"Łączenie z " + serverIp + " na porcie " + port);
            message.log(client, "Połączono z " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            out = new ObjectOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            in = new ObjectInputStream(inFromServer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Object read()
    {
        Object obj;
        try {
            while((obj = in.readObject()) != null)
            {
                return obj;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean login(String userLogin, String userPassword) {
        try {
            out.writeObject(new LoginRequest(userLogin, userPassword));
            LoginResponse loginResponse = (LoginResponse) read();
            return loginResponse.isSucces();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String userLogin, String userPassword) {
        try {
            out.writeObject(new RegisterRequest(userLogin, userPassword));
            RegistrationResponse registrationResponse = (RegistrationResponse) read();
            return registrationResponse.isSucces();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; //sprawdzic zachowanie
    }

    public boolean wyslanieTestowejWiadomosciNaSerwer(String userLogin, String text)
    {
        try {
            out.writeObject(new TestowaWiadomoscRequest(userLogin,text));
            TestowaWiadomoscResponse testowaWiadomoscResponse = (TestowaWiadomoscResponse) read();
            return testowaWiadomoscResponse.isSucces();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String odebranieTestowejWiadomosciNaSerwer(String userLogin, String text)
    {
        try {
            out.writeObject(new TestowaWiadomoscRequest(userLogin,text));
            TestowaWiadomoscResponse testowaWiadomoscResponse = (TestowaWiadomoscResponse) read();
            return testowaWiadomoscResponse.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Błąd";
    }
}
