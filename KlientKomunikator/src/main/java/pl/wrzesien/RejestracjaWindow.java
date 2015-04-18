package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-15.
 */
public class RejestracjaWindow {
    private JTextField txtLogin;
    private JPasswordField txtHaslo;
    private JButton zarejestrujButton;
    private JButton anulujButton;
    private JPanel RejestracjaWindow;

    private String serverIp;

    public RejestracjaWindow(String serverIp) {
        this.serverIp = serverIp;
        initComponents();
    }

    private void initComponents() {
        final RejestracjaWindow rejestracjaWindow = this;
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(RejestracjaWindow);
                topFrame.dispose();
                MainWindow.getFrames()[0].setVisible(true);
            }
        });

        zarejestrujButton.addActionListener(new ActionListener() {
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

    private void checkIfAllCredentialsEntered() {
        if (getLogin().isEmpty()) {
            JOptionPane.showMessageDialog(RejestracjaWindow, "Nie wpisano jeszcze loginu...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else if (getHaslo().isEmpty()) {
            JOptionPane.showMessageDialog(RejestracjaWindow, "Nie wpisano jeszcze hasła...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else {
            registerIn();
        }
    }

    private void registerIn() {
        Client c = new Client();
        if (c.connect(serverIp)) {
            if (c.register(getLogin(), getHaslo())) {
                JOptionPane.showMessageDialog(RejestracjaWindow, "Podany użytkownik już istnieje...", "Błąd rejestracji", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(RejestracjaWindow, "Rejestracja zakończyła się sukcesem...", "Rejestracja", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(RejestracjaWindow, "Nie udało się połączyć", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getLogin() {
        String text = txtLogin.getText().toString();
        System.out.println("Login: " + text);
        return text;
    }

    public String getHaslo() {
        String text = txtHaslo.getText().toString();
        System.out.println("Haslo: " + text);
        return text;
    }

    public void setTxtHaslo(JPasswordField txtHaslo) {
        this.txtHaslo = txtHaslo;
    }

    public void setTxtLogin(JTextField txtLogin) {
        this.txtLogin = txtLogin;
    }

    public JPanel getRejestracjaWindow() {
        return RejestracjaWindow;
    }
}
