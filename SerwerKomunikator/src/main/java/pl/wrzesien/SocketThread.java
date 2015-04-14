package pl.wrzesien;

import org.hibernate.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-11.
 */
public class SocketThread implements Runnable {
    private Socket socket;
    private SimpleDateFormat timeAndDate;
    private Date time;

    public String login;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    private void log(String text)
    {
        //okienko.wpis(System.currentTimeMillis() + "|" + text + "\n");
        timeAndDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss (Z)");
        time = new Date();
        //System.out.println( simpleDateHere.format(new Date()) );
        //DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        System.out.println(timeAndDate.format(time) + "|" + socket.getPort() + "|" + text);
        System.out.println();
    }

    /*boolean checkCredentials(String l, String p) {
        //hibernateutils zmienic, ¿eby sprawdza³o z bazy danych, u¿ywaj¹c podobnie jak w main
        UserService userService = new UserService();

        List<User> users = userService.showUsers();
        for (User user : users) {
            if (user.getUserNick().equals(l) && user.getUserPassword().equals(p)) {
                return true;
            }
        }

        return false;
    }*/

    @Override
    public void run() { //typ pakietu;dane.... - logowanie: login;nick;haslo, rejestracja: register;nick;haslo
        try {
            UserService us = new UserService();
            log("Polaczono z " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            //System.out.println(in.readUTF());
            String[] split = in.readUTF().split(";");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            if (split.length > 0)
            {
                switch (split[0])
                {
                    case "login":
                        Boolean check = us.checkCredentials(split[1], split[2]);
                        out.writeUTF("login;" + check.toString());
                        if (check) {
                            login = split[1];
                            log("Zalogowano uzytkownika: " + split[1]);
                            while (true) {
                                try {
                                    Thread.sleep(100);
                                    //petla obslugujaca uzytkownika (wysylanie wiadomosci etc)
                                    split = in.readUTF().split(";");
                                    switch (split[0]) {
                                        case "sendmsg":
                                            break;
                                        case "addfriend":
                                            break;
                                        default:
                                            break;
                                    }
                                } catch (InterruptedException ex) {
                                    log("Sleep zostal przerwany");
                                }
                            }
                        } else {
                            log("Bledny login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress());
                            socket.close();
                        }
                        break;
                    case "register":
                        if (split.length == 3) {
                            String login = split[1];
                            String password = split[2];

                            if (us.checkIfLoginExists(login)) {
                                out.writeUTF("error;303"); //login juz istnieje
                                log("Blad 303 - uzytkownik o podanym loginie : " + login + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress());
                                socket.close();
                            } else {
                                //UserService test = new UserService(); //- z jakiegoœ powodu w tym miejscu sesja jest wy³¹czona, gdy próbujê tworzyæ nowego u¿ytkownika
                                us.newUser(login, password);
                                out.writeUTF("register;true");
                                log("Zarejestrowano uzytkownika o loginie: " + login + " - rozlaczam z " + socket.getRemoteSocketAddress());
                                socket.close();
                            }
                        } else {
                            out.writeUTF("error;304"); //nieobslugiwany/nieznany pakiet
                            log("Nieobslugiwany/nieznany pakiet - rozlaczam z " + socket.getRemoteSocketAddress());
                            socket.close();
                        }
                        break;
                    default:
                        break;
                }
            } else
            {
                out.writeUTF("error;305"); //bledny pakiet
                log("Bledny pakiet - rozlaczam z " + socket.getRemoteSocketAddress());
                socket.close();
            }
        }
        catch(SocketException e)
        {
            if(e.toString().indexOf("Connection reset") != -1)
            {
                if (login != "")
                {
                    log("Uzytkownik " + login + " sie rozlaczyl");
                }
                else
                {
                    log("Klient sie rozlaczyl przed zalogowaniem");
                }
            }
            else
                log("Wyjatek SocketException: [" + e.toString() + "]");
        }
        catch(IOException ex)
        {
            log("Wyjatek: [" + ex.toString() + "]");
        }
    }
}