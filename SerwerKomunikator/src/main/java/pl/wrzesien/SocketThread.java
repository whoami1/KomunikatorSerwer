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
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-11.
 */
public class SocketThread implements Runnable {
    private Socket socket;
/*    private SimpleDateFormat timeAndDate;
    private Date time;*/

    private User user = new User();
    private ServerMain server;

    private Message message = new Message();
    private static final Logger logger = LoggerFactory.getLogger(SocketThread.class);

    public SocketThread(Socket socket, ServerMain server) {
        this.socket = socket;
        this.server = server;
    }

/*    private void log(String text) {
        //okienko.wpis(System.currentTimeMillis() + "|" + text + "\n");
        timeAndDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss (Z)");
        time = new Date();
        //System.out.println( simpleDateHere.format(new Date()) );
        //DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println(timeAndDate.format(time) + "|" + socket.getPort() + "|" + text);
        System.out.println();
    }*/

    @Override
    public void run() { //typ pakietu;dane.... - logowanie: login;nick;haslo, rejestracja: register;nick;haslo
        try
        {
            UserService us = new UserService();
            message.log(socket,"Polaczono z" + socket.getRemoteSocketAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object obj;

            while ((obj = ois.readObject()) != null)
            {
                if(obj instanceof LoginRequest)
                {
                    LoginRequest loginRequest = (LoginRequest) obj;
                    System.out.println(loginRequest.toString());
                    boolean success = us.checkCredentials(loginRequest.getLogin(),loginRequest.getPassword());

                    if (success)
                    {
                        oos.writeObject(new LoginResponse(success));
                        message.log(socket, "Zalogowano uzytkownika: " + loginRequest.getLogin());

                        user.setUserNick(loginRequest.getLogin());

                        server.addOnlineUser(user);
                        server.printOnlineUsers();
                    }
                    else
                    {
                        oos.writeObject(new LoginResponse(success));
                        message.log(socket,"Nieprawidlowy login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress());
                        break;
                    }
                }
                else if(obj instanceof RegisterRequest)
                {
                    RegisterRequest registerRequest = (RegisterRequest) obj;
                    System.out.println(registerRequest.toString());
                    boolean succes = us.checkIfLoginExists(registerRequest.getLogin());

                    if (succes)
                    {
                        oos.writeObject(new RegistrationResponse(succes));
                        message.log(socket,"Uzytkownik o podanym loginie: " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress());
                        break;
                    }
                    else
                    {
                        oos.writeObject(new RegistrationResponse(succes));
                        us.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        message.log(socket,"Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress());
                        break;
                    }
                }
                else if(obj instanceof TestowaWiadomoscRequest)
                {
                    TestowaWiadomoscRequest testowaWiadomoscRequest = (TestowaWiadomoscRequest) obj;
                    System.out.println(testowaWiadomoscRequest.toString());
                    boolean succes = true;
                    String innyUzytkownik = "innyuzytkownik";
                    String text = "Odpowiedz od serwera";
                    Object odpowiedz = new TestowaWiadomoscResponse(succes,innyUzytkownik,text);
                    oos.writeObject(odpowiedz);
                    System.out.println(odpowiedz);
                }
            }
            ois.close();
            socket.close();
        }
        catch(SocketException e)
        {
            if(e.toString().indexOf("Connection reset") != -1)
            {
                if (!user.getUserNick().isEmpty())
                {
                    message.log(socket,"Uzytkownik: " + user.getUserNick() + " sie rozlaczyl");

                    server.removeOnlineUser(user);
                    server.printOnlineUsers();
                }
                else
                {
                    message.log(socket,"Klient sie rozlaczyl przed zalogowaniem");
                }
            }
            else
            {
                message.log(socket,"Wyjatek SocketException: [" + e.toString() + "]");
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