package pl.wrzesien;

import pl.entity.request.LoginRequest;
import pl.entity.request.RegisterRequest;
import pl.entity.response.LoginResponse;
import pl.entity.response.RegistrationResponse;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Micha� Wrzesie� on 2015-04-11.
 */
public class SocketThread implements Runnable {
    private Socket socket;
    private SimpleDateFormat timeAndDate;
    private Date time;

    public String login;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    private void log(String text) {
        //okienko.wpis(System.currentTimeMillis() + "|" + text + "\n");
        timeAndDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss (Z)");
        time = new Date();
        //System.out.println( simpleDateHere.format(new Date()) );
        //DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println(timeAndDate.format(time) + "|" + socket.getPort() + "|" + text);
        System.out.println();
    }

    @Override
    public void run() { //typ pakietu;dane.... - logowanie: login;nick;haslo, rejestracja: register;nick;haslo
        try
        {
            UserService us = new UserService();
            log("Polaczono z " + socket.getRemoteSocketAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object obj;

            while ((obj = ois.readObject()) != null)
            {
                if(obj instanceof LoginRequest)
                {
                    LoginRequest loginRequest = (LoginRequest) obj;
                    System.out.println(loginRequest.toString());
                    login = loginRequest.getLogin();
                    boolean success = us.checkCredentials(loginRequest.getLogin(),loginRequest.getPassword());

                    if (success == true)
                    {
                        oos.writeObject(new LoginResponse(success));
                        log("Zalogowano uzytkownika: " + loginRequest.getLogin());
                    }
                    else
                    {
                        oos.writeObject(new LoginResponse(success));
                        log("Nieprawidlowy login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress());
                        //socket.close();
                    }
                }
                else if(obj instanceof RegisterRequest)
                {
                    RegisterRequest registerRequest = (RegisterRequest) obj;
                    System.out.println(registerRequest.toString());
                    boolean succes = us.checkIfLoginExists(registerRequest.getLogin());

                    if (succes == true)
                    {
                        oos.writeObject(new RegistrationResponse(succes));
                        log("Uzytkownik o podanym loginie : " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress());
                        //socket.close();
                    }
                    else
                    {
                        oos.writeObject(new RegistrationResponse(succes));
                        us.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        log("Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress());
                        //socket.close();
                    }
                }
            }
        }
        catch(SocketException e)
        {
            if(e.toString().indexOf("Connection reset") != -1)
            {
                if (!login.isEmpty())
                {
                    log("Uzytkownik: " + login + " sie rozlaczyl");
                }
                else
                {
                    log("Klient sie rozlaczyl przed zalogowaniem");
                }
            }
            else
            {
                log("Wyjatek SocketException: [" + e.toString() + "]");
                e.printStackTrace();
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}