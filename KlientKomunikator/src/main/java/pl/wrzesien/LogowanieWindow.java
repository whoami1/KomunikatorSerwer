package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-10.
 */
public class LogowanieWindow extends JFrame {
    private JPasswordField txtHaslo;
    private JTextField txtLogin;
    private JPanel LogowanieWindow;
    private JButton anulujButton;
    private JButton zalogujButton;
    private String serverIp;

    public LogowanieWindow(String serverIp) {
        this.serverIp = serverIp;
        initComponents();
    }

    private void initComponents() {
        final LogowanieWindow logowanieWindow = this;
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(LogowanieWindow);
                topFrame.dispose();
                MainWindow.getFrames()[0].setVisible(true);
            }
        });

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
            }
        });

        txtLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
            }
        });

        txtHaslo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
            }
        });
    }

    private void logIn() {
        Client client = new Client();
        if (client.connect(serverIp)) {
            if (client.login(getLogin(), getHaslo())) {
                KontaktyWindow kontaktyWindow = new KontaktyWindow(client, getLogin());
                kontaktyWindow.showKontaktyWindow();
                ((JFrame) SwingUtilities.getWindowAncestor(LogowanieWindow)).setVisible(false);
            } else {
                JOptionPane.showMessageDialog(LogowanieWindow, "Nieprawidłowy login albo hasło...", "Błąd uwierzytelniania", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(LogowanieWindow, "Połączenie nie mogło zostać zreazlizowane...", "Błąd połączenia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkIfAllCredentialsEntered() {
        if (getLogin().isEmpty()) {
            JOptionPane.showMessageDialog(LogowanieWindow, "Nie wpisano jeszcze loginu...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else if (getHaslo().isEmpty()) {
            JOptionPane.showMessageDialog(LogowanieWindow, "Nie wpisano jeszcze hasła...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else {
            logIn();
        }
    }

    public String getLogin() {
        String login = txtLogin.getText().toString();
        System.out.println("Login: " + login);
        return login;
    }

    public String getHaslo() {
        String haslo = txtHaslo.getText().toString();
        System.out.println("Haslo: " + haslo);
        return haslo;
    }

    public void setTxtHaslo(JPasswordField txtHaslo) {
        this.txtHaslo = txtHaslo;
    }

    public void setTxtLogin(JTextField txtLogin) {
        this.txtLogin = txtLogin;
    }

    public JPanel getLogowanieWindow() {
        return LogowanieWindow;
    }


}
